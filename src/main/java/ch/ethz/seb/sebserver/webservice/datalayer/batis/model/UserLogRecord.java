package ch.ethz.seb.sebserver.webservice.datalayer.batis.model;

import javax.annotation.Generated;

public class UserLogRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.user_uuid")
    private String userUuid;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.timestamp")
    private Long timestamp;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.action_type")
    private String actionType;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.entity_type")
    private String entityType;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.entity_id")
    private String entityId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.message")
    private String message;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source Table: user_log")
    public UserLogRecord(Long id, String userUuid, Long timestamp, String actionType, String entityType, String entityId, String message) {
        this.id = id;
        this.userUuid = userUuid;
        this.timestamp = timestamp;
        this.actionType = actionType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.message = message;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.user_uuid")
    public String getUserUuid() {
        return userUuid;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.action_type")
    public String getActionType() {
        return actionType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.entity_type")
    public String getEntityType() {
        return entityType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.entity_id")
    public String getEntityId() {
        return entityId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-29T16:15:37.772+01:00", comments="Source field: user_log.message")
    public String getMessage() {
        return message;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_log
     *
     * @mbg.generated Thu Nov 29 16:15:37 CET 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userUuid=").append(userUuid);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", actionType=").append(actionType);
        sb.append(", entityType=").append(entityType);
        sb.append(", entityId=").append(entityId);
        sb.append(", message=").append(message);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_log
     *
     * @mbg.generated Thu Nov 29 16:15:37 CET 2018
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserLogRecord other = (UserLogRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserUuid() == null ? other.getUserUuid() == null : this.getUserUuid().equals(other.getUserUuid()))
            && (this.getTimestamp() == null ? other.getTimestamp() == null : this.getTimestamp().equals(other.getTimestamp()))
            && (this.getActionType() == null ? other.getActionType() == null : this.getActionType().equals(other.getActionType()))
            && (this.getEntityType() == null ? other.getEntityType() == null : this.getEntityType().equals(other.getEntityType()))
            && (this.getEntityId() == null ? other.getEntityId() == null : this.getEntityId().equals(other.getEntityId()))
            && (this.getMessage() == null ? other.getMessage() == null : this.getMessage().equals(other.getMessage()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_log
     *
     * @mbg.generated Thu Nov 29 16:15:37 CET 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserUuid() == null) ? 0 : getUserUuid().hashCode());
        result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
        result = prime * result + ((getActionType() == null) ? 0 : getActionType().hashCode());
        result = prime * result + ((getEntityType() == null) ? 0 : getEntityType().hashCode());
        result = prime * result + ((getEntityId() == null) ? 0 : getEntityId().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        return result;
    }
}