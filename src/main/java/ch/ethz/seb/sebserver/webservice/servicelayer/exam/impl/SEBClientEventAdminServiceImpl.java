/*
 * Copyright (c) 2021 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sebserver.webservice.servicelayer.exam.impl;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import ch.ethz.seb.sebserver.gbl.api.APIMessage;
import ch.ethz.seb.sebserver.gbl.api.EntityType;
import ch.ethz.seb.sebserver.gbl.model.EntityKey;
import ch.ethz.seb.sebserver.gbl.model.EntityProcessingReport;
import ch.ethz.seb.sebserver.gbl.model.EntityProcessingReport.ErrorEntry;
import ch.ethz.seb.sebserver.gbl.model.exam.Exam;
import ch.ethz.seb.sebserver.gbl.model.session.ClientEvent.ExportType;
import ch.ethz.seb.sebserver.gbl.model.user.UserRole;
import ch.ethz.seb.sebserver.gbl.profile.WebServiceProfile;
import ch.ethz.seb.sebserver.gbl.util.Result;
import ch.ethz.seb.sebserver.gbl.util.Utils;
import ch.ethz.seb.sebserver.webservice.datalayer.batis.mapper.ClientEventRecordDynamicSqlSupport;
import ch.ethz.seb.sebserver.webservice.datalayer.batis.model.ClientConnectionRecord;
import ch.ethz.seb.sebserver.webservice.datalayer.batis.model.ClientEventRecord;
import ch.ethz.seb.sebserver.webservice.servicelayer.PaginationService;
import ch.ethz.seb.sebserver.webservice.servicelayer.authorization.AuthorizationService;
import ch.ethz.seb.sebserver.webservice.servicelayer.authorization.impl.SEBServerUser;
import ch.ethz.seb.sebserver.webservice.servicelayer.dao.ClientEventDAO;
import ch.ethz.seb.sebserver.webservice.servicelayer.dao.FilterMap;
import ch.ethz.seb.sebserver.webservice.servicelayer.exam.SEBClientEventAdminService;
import ch.ethz.seb.sebserver.webservice.servicelayer.exam.SEBClientEventExporter;

@Lazy
@Service
@WebServiceProfile
public class SEBClientEventAdminServiceImpl implements SEBClientEventAdminService {

    private static final Logger log = LoggerFactory.getLogger(SEBClientEventAdminServiceImpl.class);

    private final PaginationService paginationService;
    private final ClientEventDAO clientEventDAO;
    private final SEBClientEventExportTransactionHandler sebClientEventExportTransactionHandler;
    private final EnumMap<ExportType, SEBClientEventExporter> exporter;
    private final AuthorizationService authorizationService;

    public SEBClientEventAdminServiceImpl(
            final PaginationService paginationService,
            final ClientEventDAO clientEventDAO,
            final SEBClientEventExportTransactionHandler sebClientEventExportTransactionHandler,
            final Collection<SEBClientEventExporter> exporter,
            final AuthorizationService authorizationService) {

        this.paginationService = paginationService;
        this.clientEventDAO = clientEventDAO;
        this.sebClientEventExportTransactionHandler = sebClientEventExportTransactionHandler;
        this.authorizationService = authorizationService;

        this.exporter = new EnumMap<>(ExportType.class);
        exporter.forEach(exp -> this.exporter.putIfAbsent(exp.exportType(), exp));
    }

    @Override
    public Result<EntityProcessingReport> deleteAllClientEvents(final Collection<String> ids) {
        return Result.tryCatch(() -> {

            if (ids == null || ids.isEmpty()) {
                return EntityProcessingReport.ofEmptyError();
            }

            final Set<EntityKey> sources = ids.stream()
                    .map(id -> new EntityKey(id, EntityType.CLIENT_EVENT))
                    .collect(Collectors.toSet());

            final Result<Collection<EntityKey>> delete = this.clientEventDAO.delete(sources);
            if (delete.hasError()) {
                return new EntityProcessingReport(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Arrays.asList(new ErrorEntry(null, APIMessage.ErrorMessage.UNEXPECTED.of(delete.getError()))));
            } else {
                return new EntityProcessingReport(
                        sources,
                        delete.get(),
                        Collections.emptyList());
            }
        });
    }

    @Override
    public void exportSEBClientLogs(
            final OutputStream output,
            final FilterMap filterMap,
            final String sort,
            final ExportType exportType,
            final boolean includeConnectionDetails,
            final boolean includeExamDetails) {

        new exportRunner(
                this.exporter.get(exportType),
                includeConnectionDetails,
                includeExamDetails,
                new Pager(filterMap, sort),
                output)
                        .run();

    }

    private class exportRunner {

        private final SEBClientEventExporter exporter;
        private final boolean includeConnectionDetails;
        private final boolean includeExamDetails;
        private final Iterator<Collection<ClientEventRecord>> pager;
        private final OutputStream output;

        private final Map<Long, Exam> examCache;
        private final Map<Long, ClientConnectionRecord> connectionCache;

        public exportRunner(
                final SEBClientEventExporter exporter,
                final boolean includeConnectionDetails,
                final boolean includeExamDetails,
                final Iterator<Collection<ClientEventRecord>> pager,
                final OutputStream output) {

            this.exporter = exporter;
            this.includeConnectionDetails = includeConnectionDetails;
            this.includeExamDetails = includeExamDetails;
            this.pager = pager;
            this.output = output;

            this.connectionCache = includeConnectionDetails ? new HashMap<>() : null;
            this.examCache = includeExamDetails ? new HashMap<>() : null;
        }

        public void run() {

            final SEBServerUser currentUser = SEBClientEventAdminServiceImpl.this.authorizationService
                    .getUserService()
                    .getCurrentUser();
            final EnumSet<UserRole> userRoles = currentUser.getUserRoles();
            final boolean isSupporterOnly = userRoles.size() == 1 && userRoles.contains(UserRole.EXAM_SUPPORTER);

            // first stream header line
            this.exporter.streamHeader(this.output, this.includeConnectionDetails, this.includeExamDetails);

            // then batch with the pager and stream line per line
            while (this.pager.hasNext()) {
                this.pager.next().forEach(rec -> {

                    final Exam exam = getExam(rec.getClientConnectionId());

                    if (!isSupporterOnly || exam.isOwner(currentUser.uuid())) {
                        this.exporter.streamData(
                                this.output,
                                rec,
                                this.includeConnectionDetails ? getConnection(rec.getClientConnectionId()) : null,
                                this.includeExamDetails ? getExam(rec.getClientConnectionId()) : null);
                    }
                });
            }
        }

        private ClientConnectionRecord getConnection(final Long connectionId) {
            if (!this.connectionCache.containsKey(connectionId)) {
                SEBClientEventAdminServiceImpl.this.sebClientEventExportTransactionHandler
                        .clientConnectionById(connectionId)
                        .map(e -> this.connectionCache.put(connectionId, e))
                        .onError(error -> log.error("Failed to get ClientConnectionRecord for id: {}",
                                connectionId,
                                error));
            }

            return this.connectionCache.get(connectionId);
        }

        private Exam getExam(final Long connectionId) {
            final ClientConnectionRecord connection = getConnection(connectionId);
            final Long examId = connection.getExamId();
            if (!this.examCache.containsKey(examId)) {
                SEBClientEventAdminServiceImpl.this.sebClientEventExportTransactionHandler
                        .examById(examId)
                        .map(e -> this.examCache.put(examId, e))
                        .onError(error -> log.error("Failed to get Exam for id: {}",
                                examId,
                                error));
            }

            return this.examCache.get(examId);
        }
    }

    private class Pager implements Iterator<Collection<ClientEventRecord>> {

        private final FilterMap filterMap;
        private final String sort;

        private int pageNumber = 0;
        private final int pageSize = 100;

        private Collection<ClientEventRecord> nextRecords;

        public Pager(
                final FilterMap filterMap,
                final String sort) {

            this.filterMap = filterMap;
            this.sort = sort;

            fetchNext();
        }

        @Override
        public boolean hasNext() {
            return this.nextRecords != null && !this.nextRecords.isEmpty();
        }

        @Override
        public Collection<ClientEventRecord> next() {
            final Collection<ClientEventRecord> result = this.nextRecords;
            fetchNext();
            return result;
        }

        private void fetchNext() {
            try {

                this.nextRecords = SEBClientEventAdminServiceImpl.this.paginationService.fetch(
                        this.pageNumber,
                        this.pageSize,
                        this.sort,
                        ClientEventRecordDynamicSqlSupport.clientEventRecord.name(),
                        () -> SEBClientEventAdminServiceImpl.this.sebClientEventExportTransactionHandler
                                .allMatching(this.filterMap, Utils.truePredicate()))
                        .getOrThrow();

                this.pageNumber++;

            } catch (final Exception e) {
                log.error("Failed to fetch next batch: ", e);
                this.nextRecords = null;
            }
        }
    }

}
