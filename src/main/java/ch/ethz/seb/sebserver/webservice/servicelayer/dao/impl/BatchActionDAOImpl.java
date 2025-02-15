/*
 * Copyright (c) 2021 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sebserver.webservice.servicelayer.dao.impl;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.ethz.seb.sebserver.gbl.Constants;
import ch.ethz.seb.sebserver.gbl.api.API.BatchActionType;
import ch.ethz.seb.sebserver.gbl.api.EntityType;
import ch.ethz.seb.sebserver.gbl.model.BatchAction;
import ch.ethz.seb.sebserver.gbl.model.Domain;
import ch.ethz.seb.sebserver.gbl.model.EntityKey;
import ch.ethz.seb.sebserver.gbl.profile.WebServiceProfile;
import ch.ethz.seb.sebserver.gbl.util.Result;
import ch.ethz.seb.sebserver.gbl.util.Utils;
import ch.ethz.seb.sebserver.webservice.datalayer.batis.mapper.BatchActionRecordDynamicSqlSupport;
import ch.ethz.seb.sebserver.webservice.datalayer.batis.mapper.BatchActionRecordMapper;
import ch.ethz.seb.sebserver.webservice.datalayer.batis.model.BatchActionRecord;
import ch.ethz.seb.sebserver.webservice.servicelayer.dao.BatchActionDAO;
import ch.ethz.seb.sebserver.webservice.servicelayer.dao.DAOLoggingSupport;
import ch.ethz.seb.sebserver.webservice.servicelayer.dao.FilterMap;
import ch.ethz.seb.sebserver.webservice.servicelayer.dao.ResourceNotFoundException;
import ch.ethz.seb.sebserver.webservice.servicelayer.dao.TransactionHandler;

@Lazy
@Component
@WebServiceProfile
public class BatchActionDAOImpl implements BatchActionDAO {

    private final BatchActionRecordMapper batchActionRecordMapper;

    public BatchActionDAOImpl(final BatchActionRecordMapper batchActionRecordMapper) {
        this.batchActionRecordMapper = batchActionRecordMapper;
    }

    @Override
    public EntityType entityType() {
        return EntityType.BATCH_ACTION;
    }

    @Override
    @Transactional
    public Result<BatchAction> getAndReserveNext(final String processId) {
        return Result.tryCatch(() -> {

            final Long oldTherhold = Utils.getMillisecondsNow() - Constants.HOUR_IN_MILLIS;
            final List<BatchActionRecord> next = this.batchActionRecordMapper.selectByExample()
                    .where(BatchActionRecordDynamicSqlSupport.lastUpdate, isNull())
                    .and(BatchActionRecordDynamicSqlSupport.processorId, isNull())
                    .or(BatchActionRecordDynamicSqlSupport.lastUpdate, isLessThan(oldTherhold))
                    .build()
                    .execute();

            if (next == null || next.isEmpty()) {

                throw new ResourceNotFoundException(
                        EntityType.BATCH_ACTION,
                        processId);
            }

            final BatchActionRecord nextRec = next.get(0);

            final BatchActionRecord newRecord = new BatchActionRecord(
                    nextRec.getId(),
                    null,
                    null,
                    null,
                    null,
                    Utils.getMillisecondsNow(),
                    processId);

            this.batchActionRecordMapper.updateByPrimaryKeySelective(newRecord);
            return this.batchActionRecordMapper.selectByPrimaryKey(nextRec.getId());
        })
                .flatMap(this::toDomainModel)
                .onError(TransactionHandler::rollback);
    }

    @Override
    @Transactional
    public Result<BatchAction> updateProgress(
            final Long actionId,
            final String processId,
            final Collection<String> modelIds) {

        return Result.tryCatch(() -> {

            final BatchActionRecord rec = this.batchActionRecordMapper.selectByPrimaryKey(actionId);

            if (!processId.equals(rec.getProcessorId())) {
                throw new RuntimeException("Batch action processor id mismatch: " + processId + " " + rec);
            }

            final Set<String> ids = new HashSet<>(Arrays.asList(StringUtils.split(
                    rec.getSuccessful(),
                    Constants.LIST_SEPARATOR)));
            ids.addAll(modelIds);

            final BatchActionRecord newRecord = new BatchActionRecord(
                    actionId,
                    null,
                    null,
                    null,
                    StringUtils.join(ids, Constants.LIST_SEPARATOR),
                    Utils.getMillisecondsNow(),
                    processId);

            this.batchActionRecordMapper.updateByPrimaryKeySelective(newRecord);
            return this.batchActionRecordMapper.selectByPrimaryKey(actionId);
        })
                .flatMap(this::toDomainModel)
                .onError(TransactionHandler::rollback);
    }

    @Override
    @Transactional
    public void setSuccessfull(final Long actionId, final String processId, final String modelId) {
        try {

            final BatchActionRecord rec = this.batchActionRecordMapper.selectByPrimaryKey(actionId);

            if (!processId.equals(rec.getProcessorId())) {
                throw new RuntimeException("Batch action processor id mismatch: " + processId + " " + rec);
            }

            final BatchActionRecord newRecord = new BatchActionRecord(
                    actionId,
                    null,
                    null,
                    null,
                    rec.getSuccessful() + Constants.LIST_SEPARATOR + modelId,
                    Utils.getMillisecondsNow(),
                    processId);
            this.batchActionRecordMapper.updateByPrimaryKeySelective(newRecord);

        } catch (final Exception e) {
            log.error("Failed to mark entity sucessfuly processed: modelId: {}, processId");
        }
    }

    @Override
    @Transactional
    public Result<BatchAction> finishUp(final Long actionId, final String processId, final boolean force) {
        return Result.tryCatch(() -> {

            final BatchActionRecord rec = this.batchActionRecordMapper.selectByPrimaryKey(actionId);

            if (!processId.equals(rec.getProcessorId())) {
                throw new RuntimeException("Batch action processor id mismatch: " + processId + " " + rec);
            }

            if (!force) {
                // apply consistency check
                final Set<String> ids = new HashSet<>(Arrays.asList(StringUtils.split(
                        rec.getSourceIds(),
                        Constants.LIST_SEPARATOR)));

                final Set<String> success = new HashSet<>(Arrays.asList(StringUtils.split(
                        rec.getSourceIds(),
                        Constants.LIST_SEPARATOR)));

                if (ids.size() != success.size()) {
                    throw new IllegalStateException(
                            "Processing ids mismatch source: " + ids + " success: " + success);
                }
            }

            final BatchActionRecord newRecord = new BatchActionRecord(
                    actionId,
                    null,
                    null,
                    null,
                    null,
                    Utils.getMillisecondsNow(),
                    processId + FLAG_FINISHED);

            this.batchActionRecordMapper.updateByPrimaryKeySelective(newRecord);
            return this.batchActionRecordMapper.selectByPrimaryKey(actionId);
        })
                .flatMap(this::toDomainModel)
                .onError(TransactionHandler::rollback);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<BatchAction> byPK(final Long id) {
        return recordById(id)
                .flatMap(this::toDomainModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Collection<BatchAction>> allOf(final Set<Long> pks) {
        return Result.tryCatch(() -> {
            if (pks == null || pks.isEmpty()) {
                return Collections.emptyList();
            }

            return this.batchActionRecordMapper.selectByExample()
                    .where(BatchActionRecordDynamicSqlSupport.id, isIn(new ArrayList<>(pks)))
                    .build()
                    .execute()
                    .stream()
                    .map(this::toDomainModel)
                    .flatMap(DAOLoggingSupport::logAndSkipOnError)
                    .collect(Collectors.toList());
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Collection<BatchAction>> allMatching(
            final FilterMap filterMap,
            final Predicate<BatchAction> predicate) {

        return Result.tryCatch(() -> this.batchActionRecordMapper
                .selectByExample()
                .where(
                        BatchActionRecordDynamicSqlSupport.institutionId,
                        isEqualToWhenPresent(filterMap.getInstitutionId()))
                .and(
                        BatchActionRecordDynamicSqlSupport.actionType,
                        SqlBuilder.isEqualToWhenPresent(
                                filterMap.getString(Domain.BATCH_ACTION.ATTR_ACTION_TYPE)))
                .build()
                .execute()
                .stream()
                .map(this::toDomainModel)
                .flatMap(DAOLoggingSupport::logAndSkipOnError)
                .filter(predicate)
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public Result<BatchAction> createNew(final BatchAction data) {
        return Result.tryCatch(() -> {

            final BatchActionRecord newRecord = new BatchActionRecord(
                    null,
                    data.institutionId,
                    data.actionType.toString(),
                    StringUtils.join(data.sourceIds, Constants.LIST_SEPARATOR),
                    null, null, null);

            this.batchActionRecordMapper.insert(newRecord);
            return newRecord;
        })
                .flatMap(this::toDomainModel)
                .onError(TransactionHandler::rollback);
    }

    @Override
    @Transactional
    public Result<BatchAction> save(final BatchAction data) {
        return Result.tryCatch(() -> {

            final BatchActionRecord newRecord = new BatchActionRecord(
                    data.id,
                    null,
                    null,
                    null,
                    StringUtils.join(data.successful, Constants.LIST_SEPARATOR),
                    data.getLastUpdate(),
                    data.processorId);

            this.batchActionRecordMapper.updateByPrimaryKeySelective(newRecord);
            return this.batchActionRecordMapper.selectByPrimaryKey(data.id);
        })
                .flatMap(this::toDomainModel)
                .onError(TransactionHandler::rollback);
    }

    @Override
    @Transactional
    public Result<Collection<EntityKey>> delete(final Set<EntityKey> all) {
        return Result.tryCatch(() -> {

            final List<Long> ids = extractListOfPKs(all);

            if (ids.isEmpty()) {
                return Collections.emptyList();
            }

            this.batchActionRecordMapper.deleteByExample()
                    .where(BatchActionRecordDynamicSqlSupport.id, isIn(ids))
                    .build()
                    .execute();

            return ids.stream()
                    .map(id -> new EntityKey(id, EntityType.BATCH_ACTION))
                    .collect(Collectors.toList());
        });
    }

    private Result<BatchActionRecord> recordById(final Long id) {
        return Result.tryCatch(() -> {
            final BatchActionRecord record = this.batchActionRecordMapper.selectByPrimaryKey(id);
            if (record == null) {
                throw new ResourceNotFoundException(
                        EntityType.BATCH_ACTION,
                        String.valueOf(id));
            }
            return record;
        });
    }

    private Result<BatchAction> toDomainModel(final BatchActionRecord record) {
        return Result.tryCatch(() -> new BatchAction(
                record.getId(),
                record.getInstitutionId(),
                BatchActionType.valueOf(record.getActionType()),
                Arrays.asList(record.getSourceIds().split(Constants.LIST_SEPARATOR)),
                Arrays.asList(record.getSuccessful().split(Constants.LIST_SEPARATOR)),
                record.getLastUpdate(),
                record.getProcessorId()));
    }

}
