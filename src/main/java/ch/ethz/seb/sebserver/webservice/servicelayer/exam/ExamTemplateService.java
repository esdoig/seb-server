/*
 * Copyright (c) 2021 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sebserver.webservice.servicelayer.exam;

import ch.ethz.seb.sebserver.gbl.model.exam.Exam;
import ch.ethz.seb.sebserver.gbl.util.Result;

public interface ExamTemplateService {

    String VAR_START_DATE = "__startDate__";
    String VAR_CURRENT_DATE = "__currentDate__";
    String VAR_EXAM_NAME = "__examName__";
    String VAR_EXAM_TEMPLATE_NAME = "__examTemplateName__";

    String DEFAULT_EXAM_CONFIG_NAME_TEMPLATE = VAR_START_DATE + " " + VAR_EXAM_NAME;
    String DEFAULT_EXAM_CONFIG_DESC_TEMPLATE =
            "This has automatically been created from the exam template: "
                    + VAR_EXAM_TEMPLATE_NAME + " at: "
                    + VAR_CURRENT_DATE;

    /** Adds the indicators that are defined by a exam template or
     * a default indicator that is defined by configuration to a given exam.
     *
     * @param exam The Exam to add the default indicator
     * @return Result refer to the Exam with added default indicator or to an error if happened */
    Result<Exam> addDefinedIndicators(Exam exam);

    /** Initializes additional attributes for a specified Exam on creation.
     *
     * @param exam The Exam to add the default indicator
     * @return Result refer to the created exam or to an error when happened */
    Result<Exam> initAdditionalAttributes(Exam exam);

    /** Initializes a pre defined exam configuration. The configuration template to create a exam configuration
     * is defined by a given linked exam template. This is used to create the exam configuration and automatically
     * link it to the newly created exam
     *
     * @param exam The Exam to create and add new exam configuration
     * @return Result refer to the created exam or to an error when happened */
    Result<Exam> initExamConfiguration(Exam exam);

}
