/*
 * Copyright (c) 2020 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sebserver.gui.form;

import ch.ethz.seb.sebserver.gui.service.i18n.LocTextKey;
import ch.ethz.seb.sebserver.gui.widget.PasswordInput;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class PasswordFieldBuilder extends FieldBuilder<CharSequence> {

    PasswordFieldBuilder(final String name, final LocTextKey label, final CharSequence value) {
        super(name, label, value);
    }

    @Override
    void build(FormBuilder builder) {
        final boolean readonly = builder.readonly || this.readonly;
        final Control titleLabel = createTitleLabel(builder.formParent, builder, this);
        final Composite fieldGrid = createFieldGrid(builder.formParent, this.spanInput);

        final PasswordInput input = new PasswordInput(fieldGrid, builder.widgetFactory);
        input.setEditable(!readonly);
        input.setValue((StringUtils.isNotBlank(this.value))
                ? builder.cryptor.decrypt(this.value)
                : this.value);

        final Label errorLabel = createErrorLabel(fieldGrid);
        builder.form.putField(this.name, titleLabel, input, errorLabel);
    }
}
