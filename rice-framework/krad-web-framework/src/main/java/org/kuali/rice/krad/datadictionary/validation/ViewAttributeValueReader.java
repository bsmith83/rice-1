/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.datadictionary.validation;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.capability.CaseConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.MustOccurConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.PrerequisiteConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.SimpleConstrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.ValidCharactersConstrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.CaseConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.MustOccurConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.SimpleConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.lifecycle.ViewPostMetadata;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AttributeValueReader which can read the correct values from all InputFields which exist on the View
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewAttributeValueReader extends BaseAttributeValueReader {
    private ViewModel form;

    private List<Constrainable> inputFields = new ArrayList<Constrainable>();
    private Map<String, InputFieldConstrainableInfo> inputFieldMap = new HashMap<String, InputFieldConstrainableInfo>();

    /**
     * Constructor for ViewAttributeValueReader, the View must already be indexed and
     * the InputFields must have already be initialized for this reader to work properly
     *
     * @param form model object representing the View's form data
     */
    public ViewAttributeValueReader(ViewModel form) {
        this.form = form;

        ViewPostMetadata viewPostMetadata = form.getViewPostMetadata();

        for (String id: viewPostMetadata.getInputFieldIds()) {
            InputFieldConstrainableInfo info = new InputFieldConstrainableInfo();

            Object label = viewPostMetadata.getComponentPostData(id, "label");
            if (label != null) {
                info.setLabel((String)label);
            }

            Object name = viewPostMetadata.getComponentPostData(id, "path");
            if (name != null) {
                info.setName((String) name);
            }

            Object validCharactersConstraint = viewPostMetadata.getComponentPostData(id, "validCharactersConstraint");
            if (validCharactersConstraint != null) {
                info.setValidCharactersConstraint((ValidCharactersConstraint)validCharactersConstraint);
            }

            Object caseConstraint = viewPostMetadata.getComponentPostData(id, "caseConstraint");
            if (caseConstraint != null) {
                info.setCaseConstraint((CaseConstraint)caseConstraint);
            }

            Object prerequisiteConstraints = viewPostMetadata.getComponentPostData(id, "prerequisiteConstraints");
            if (prerequisiteConstraints != null) {
                info.setPrerequisiteConstraints((List<PrerequisiteConstraint>)prerequisiteConstraints);
            }

            Object mustOccurConstraints = viewPostMetadata.getComponentPostData(id, "mustOccurConstraints");
            if (mustOccurConstraints != null) {
                info.setMustOccurConstraints((List<MustOccurConstraint>)mustOccurConstraints);
            }

            Object simpleConstraint = viewPostMetadata.getComponentPostData(id, "simpleConstraint");
            if (simpleConstraint != null) {
                info.setSimpleConstraint((SimpleConstraint)simpleConstraint);
            }

            inputFields.add(info);
            inputFieldMap.put(info.getName(), info);
        }
    }

    /**
     * Gets the definition which is an InputField on the View/Page
     */
    @Override
    public Constrainable getDefinition(String attributeName) {
        InputFieldConstrainableInfo field = inputFieldMap.get(attributeName);
        if (field != null) {
            return field;
        } else {
            return null;
        }
    }

    /**
     * Gets all InputFields (which extend Constrainable)
     *
     * @return constrainable input fields
     */
    @Override
    public List<Constrainable> getDefinitions() {
        return inputFields;
    }

    /**
     * Returns the label associated with the InputField which has that AttributeName
     *
     * @param attributeName attribute name
     * @return label associated with the named attribute
     */
    @Override
    public String getLabel(String attributeName) {
        InputFieldConstrainableInfo field = inputFieldMap.get(attributeName);
        if (field != null) {
            return field.getLabel();
        } else {
            return "";
        }
    }

    /**
     * Returns the Form object
     *
     * @return form set in the constructor
     */
    @Override
    public Object getObject() {
        return form;
    }

    /**
     * Not used for this reader, returns null
     *
     * @return null
     */
    @Override
    public Constrainable getEntry() {
        return null;
    }

    /**
     * Returns current attributeName which represents the path
     *
     * @return attributeName set on this reader
     */
    @Override
    public String getPath() {
        return this.attributeName;
    }

    /**
     * Gets the type of value for this AttributeName as represented on the Form
     *
     * @param attributeName
     * @return attribute type
     */
    @Override
    public Class<?> getType(String attributeName) {
        Object fieldValue = ObjectPropertyUtils.getPropertyValue(form, attributeName);
        return fieldValue.getClass();
    }

    /**
     * If the current attribute being evaluated is a field of an addLine return false because it should not
     * be evaluated during Validation.
     *
     * @return false if InputField is part of an addLine for a collection, true otherwise
     */
    @Override
    public boolean isReadable() {
        if (attributeName != null && attributeName.contains(UifPropertyPaths.NEW_COLLECTION_LINES)) {
            return false;
        }
        return true;
    }

    /**
     * Return value of the field for the attributeName currently set on this reader
     *
     * @param <X> return type
     * @return value of the field for the attributeName currently set on this reader
     * @throws AttributeValidationException
     */
    @Override
    public <X> X getValue() throws AttributeValidationException {
        X fieldValue = null;
        if (StringUtils.isNotBlank(this.attributeName)) {
            fieldValue = ObjectPropertyUtils.<X>getPropertyValue(form, this.attributeName);
        }
        return fieldValue;
    }

    /**
     * Return value of the field for the attributeName passed in
     *
     * @param attributeName name (which represents a path) of the value to be retrieved on the Form
     * @param <X> return type
     * @return value of that attributeName represents on the form
     * @throws AttributeValidationException
     */
    @Override
    public <X> X getValue(String attributeName) throws AttributeValidationException {
        X fieldValue = null;
        if (StringUtils.isNotBlank(attributeName)) {
            fieldValue = ObjectPropertyUtils.<X>getPropertyValue(form, this.attributeName);
        }
        return fieldValue;
    }

    /**
     * Cones this AttributeValueReader
     *
     * @return AttributeValueReader
     */
    @Override
    public AttributeValueReader clone() {
        ViewAttributeValueReader clone = new ViewAttributeValueReader(form);
        clone.setAttributeName(this.attributeName);
        return clone;
    }

    public class InputFieldConstrainableInfo implements SimpleConstrainable, CaseConstrainable, PrerequisiteConstrainable, MustOccurConstrainable, ValidCharactersConstrainable {

        private String label;
        private String name;
        private ValidCharactersConstraint validCharactersConstraint;
        private CaseConstraint caseConstraint;
        private List<PrerequisiteConstraint> prerequisiteConstraints;
        private List<MustOccurConstraint> mustOccurConstraints;
        private SimpleConstraint simpleConstraint;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ValidCharactersConstraint getValidCharactersConstraint() {
            return validCharactersConstraint;
        }

        public void setValidCharactersConstraint(ValidCharactersConstraint validCharactersConstraint) {
            this.validCharactersConstraint = validCharactersConstraint;
        }

        public CaseConstraint getCaseConstraint() {
            return caseConstraint;
        }

        public void setCaseConstraint(CaseConstraint caseConstraint) {
            this.caseConstraint = caseConstraint;
        }

        public List<PrerequisiteConstraint> getPrerequisiteConstraints() {
            return prerequisiteConstraints;
        }

        public void setPrerequisiteConstraints(List<PrerequisiteConstraint> prerequisiteConstraints) {
            this.prerequisiteConstraints = prerequisiteConstraints;
        }

        public List<MustOccurConstraint> getMustOccurConstraints() {
            return mustOccurConstraints;
        }

        public void setMustOccurConstraints(List<MustOccurConstraint> mustOccurConstraints) {
            this.mustOccurConstraints = mustOccurConstraints;
        }

        public SimpleConstraint getSimpleConstraint() {
            return simpleConstraint;
        }

        public void setSimpleConstraint(SimpleConstraint simpleConstraint) {
            this.simpleConstraint = simpleConstraint;
        }
    }

}
