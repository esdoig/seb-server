package ch.ethz.seb.sebserver.gbl.model;

import javax.annotation.Generated;

/** Defines the global names of the domain model and domain model fields.
* This shall be used as a static overall domain model names reference within SEB Server Web-Service as well as within the integrated GUI
* This file is generated by the org.eth.demo.sebserver.gen.DomainModelNameReferencePlugin and must not be edited manually.**/
@Generated(value="org.mybatis.generator.api.MyBatisGenerator",comments="ch.ethz.seb.sebserver.gen.DomainModelNameReferencePlugin",date="2019-04-17T16:13:33.863+02:00")
public interface Domain {

    interface CONFIGURATION_ATTRIBUTE {
        String TYPE_NAME = "ConfigurationAttribute";
        String REFERENCE_NAME = "configurationAttributes";
        String ATTR_ID = "id";
        String ATTR_NAME = "name";
        String ATTR_TYPE = "type";
        String ATTR_PARENT_ID = "parentId";
        String ATTR_RESOURCES = "resources";
        String ATTR_VALIDATOR = "validator";
        String ATTR_DEPENDENCIES = "dependencies";
        String ATTR_DEFAULT_VALUE = "defaultValue";
    }

    interface CONFIGURATION_VALUE {
        String TYPE_NAME = "ConfigurationValue";
        String REFERENCE_NAME = "configurationValues";
        String ATTR_ID = "id";
        String ATTR_INSTITUTION_ID = "institutionId";
        String ATTR_CONFIGURATION_ID = "configurationId";
        String ATTR_CONFIGURATION_ATTRIBUTE_ID = "configurationAttributeId";
        String ATTR_LIST_INDEX = "listIndex";
        String ATTR_VALUE = "value";
        String ATTR_TEXT = "text";
    }

    interface ORIENTATION {
        String TYPE_NAME = "Orientation";
        String REFERENCE_NAME = "orientations";
        String ATTR_ID = "id";
        String ATTR_CONFIG_ATTRIBUTE_ID = "configAttributeId";
        String ATTR_TEMPLATE_ID = "templateId";
        String ATTR_VIEW = "view";
        String ATTR_GROUP = "group";
        String ATTR_X_POSITION = "xPosition";
        String ATTR_Y_POSITION = "yPosition";
        String ATTR_WIDTH = "width";
        String ATTR_HEIGHT = "height";
    }

    interface CONFIGURATION {
        String TYPE_NAME = "Configuration";
        String REFERENCE_NAME = "configurations";
        String ATTR_ID = "id";
        String ATTR_INSTITUTION_ID = "institutionId";
        String ATTR_CONFIGURATION_NODE_ID = "configurationNodeId";
        String ATTR_VERSION = "version";
        String ATTR_VERSION_DATE = "versionDate";
        String ATTR_FOLLOWUP = "followup";
    }

    interface CONFIGURATION_NODE {
        String TYPE_NAME = "ConfigurationNode";
        String REFERENCE_NAME = "configurationNodes";
        String ATTR_ID = "id";
        String ATTR_INSTITUTION_ID = "institutionId";
        String ATTR_TEMPLATE_ID = "templateId";
        String ATTR_OWNER = "owner";
        String ATTR_NAME = "name";
        String ATTR_DESCRIPTION = "description";
        String ATTR_TYPE = "type";
        String ATTR_ACTIVE = "active";
    }

    interface EXAM_CONFIGURATION_MAP {
        String TYPE_NAME = "ExamConfigurationMap";
        String REFERENCE_NAME = "examConfigurationMaps";
        String ATTR_ID = "id";
        String ATTR_INSTITUTION_ID = "institutionId";
        String ATTR_EXAM_ID = "examId";
        String ATTR_CONFIGURATION_NODE_ID = "configurationNodeId";
        String ATTR_USER_NAMES = "userNames";
    }

    interface EXAM {
        String TYPE_NAME = "Exam";
        String REFERENCE_NAME = "exams";
        String ATTR_ID = "id";
        String ATTR_INSTITUTION_ID = "institutionId";
        String ATTR_LMS_SETUP_ID = "lmsSetupId";
        String ATTR_EXTERNAL_ID = "externalId";
        String ATTR_OWNER = "owner";
        String ATTR_SUPPORTER = "supporter";
        String ATTR_TYPE = "type";
        String ATTR_QUIT_PASSWORD = "quitPassword";
        String ATTR_ACTIVE = "active";
    }

    interface CLIENT_CONNECTION {
        String TYPE_NAME = "ClientConnection";
        String REFERENCE_NAME = "clientConnections";
        String ATTR_ID = "id";
        String ATTR_EXAM_ID = "examId";
        String ATTR_STATUS = "status";
        String ATTR_CONNECTION_TOKEN = "connectionToken";
        String ATTR_USER_NAME = "userName";
        String ATTR_VDI = "vdi";
        String ATTR_CLIENT_ADDRESS = "clientAddress";
        String ATTR_VIRTUAL_CLIENT_ADDRESS = "virtualClientAddress";
    }

    interface CLIENT_EVENT {
        String TYPE_NAME = "ClientEvent";
        String REFERENCE_NAME = "clientEvents";
        String ATTR_ID = "id";
        String ATTR_CONNECTION_ID = "connectionId";
        String ATTR_USER_IDENTIFIER = "userIdentifier";
        String ATTR_TYPE = "type";
        String ATTR_TIMESTAMP = "timestamp";
        String ATTR_NUMERIC_VALUE = "numericValue";
        String ATTR_TEXT = "text";
    }

    interface INDICATOR {
        String TYPE_NAME = "Indicator";
        String REFERENCE_NAME = "indicators";
        String ATTR_ID = "id";
        String ATTR_EXAM_ID = "examId";
        String ATTR_TYPE = "type";
        String ATTR_NAME = "name";
        String ATTR_COLOR = "color";
    }

    interface THRESHOLD {
        String TYPE_NAME = "Threshold";
        String REFERENCE_NAME = "thresholds";
        String ATTR_ID = "id";
        String ATTR_INDICATOR_ID = "indicatorId";
        String ATTR_VALUE = "value";
        String ATTR_COLOR = "color";
    }

    interface INSTITUTION {
        String TYPE_NAME = "Institution";
        String REFERENCE_NAME = "institutions";
        String ATTR_ID = "id";
        String ATTR_NAME = "name";
        String ATTR_URL_SUFFIX = "urlSuffix";
        String ATTR_THEME_NAME = "themeName";
        String ATTR_ACTIVE = "active";
        String ATTR_LOGO_IMAGE = "logoImage";
    }

    interface SEB_CLIENT_CONFIGURATION {
        String TYPE_NAME = "SebClientConfiguration";
        String REFERENCE_NAME = "sebClientConfigurations";
        String ATTR_ID = "id";
        String ATTR_INSTITUTION_ID = "institutionId";
        String ATTR_NAME = "name";
        String ATTR_DATE = "date";
        String ATTR_CLIENT_NAME = "clientName";
        String ATTR_CLIENT_SECRET = "clientSecret";
        String ATTR_ENCRYPT_SECRET = "encryptSecret";
        String ATTR_ACTIVE = "active";
    }

    interface LMS_SETUP {
        String TYPE_NAME = "LmsSetup";
        String REFERENCE_NAME = "lmsSetups";
        String ATTR_ID = "id";
        String ATTR_INSTITUTION_ID = "institutionId";
        String ATTR_NAME = "name";
        String ATTR_LMS_TYPE = "lmsType";
        String ATTR_LMS_URL = "lmsUrl";
        String ATTR_LMS_CLIENTNAME = "lmsClientname";
        String ATTR_LMS_CLIENTSECRET = "lmsClientsecret";
        String ATTR_LMS_REST_API_TOKEN = "lmsRestApiToken";
        String ATTR_ACTIVE = "active";
    }

    interface USER {
        String TYPE_NAME = "User";
        String REFERENCE_NAME = "users";
        String ATTR_ID = "id";
        String ATTR_INSTITUTION_ID = "institutionId";
        String ATTR_UUID = "uuid";
        String ATTR_NAME = "name";
        String ATTR_USERNAME = "username";
        String ATTR_PASSWORD = "password";
        String ATTR_EMAIL = "email";
        String ATTR_LANGUAGE = "language";
        String ATTR_TIMEZONE = "timezone";
        String ATTR_ACTIVE = "active";
    }

    interface USER_ROLE {
        String TYPE_NAME = "UserRole";
        String REFERENCE_NAME = "userRoles";
        String ATTR_ID = "id";
        String ATTR_USER_ID = "userId";
        String ATTR_ROLE_NAME = "roleName";
    }

    interface USER_ACTIVITY_LOG {
        String TYPE_NAME = "UserActivityLog";
        String REFERENCE_NAME = "userActivityLogs";
        String ATTR_ID = "id";
        String ATTR_USER_UUID = "userUuid";
        String ATTR_TIMESTAMP = "timestamp";
        String ATTR_ACTIVITY_TYPE = "activityType";
        String ATTR_ENTITY_TYPE = "entityType";
        String ATTR_ENTITY_ID = "entityId";
        String ATTR_MESSAGE = "message";
    }

    interface ADDITIONAL_ATTRIBUTES {
        String TYPE_NAME = "AdditionalAttributes";
        String REFERENCE_NAME = "additionalAttributess";
        String ATTR_ID = "id";
        String ATTR_ENTITY_TYPE = "entityType";
        String ATTR_ENTITY_ID = "entityId";
        String ATTR_NAME = "name";
        String ATTR_VALUE = "value";
    }
}