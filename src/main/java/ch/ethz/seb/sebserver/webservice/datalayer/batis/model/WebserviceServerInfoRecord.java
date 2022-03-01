package ch.ethz.seb.sebserver.webservice.datalayer.batis.model;

import javax.annotation.Generated;

public class WebserviceServerInfoRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source field: webservice_server_info.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source field: webservice_server_info.uuid")
    private String uuid;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source field: webservice_server_info.service_address")
    private String serviceAddress;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source field: webservice_server_info.master")
    private Integer master;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source field: webservice_server_info.update_time")
    private Long updateTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source Table: webservice_server_info")
    public WebserviceServerInfoRecord(Long id, String uuid, String serviceAddress, Integer master, Long updateTime) {
        this.id = id;
        this.uuid = uuid;
        this.serviceAddress = serviceAddress;
        this.master = master;
        this.updateTime = updateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source field: webservice_server_info.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source field: webservice_server_info.uuid")
    public String getUuid() {
        return uuid;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source field: webservice_server_info.service_address")
    public String getServiceAddress() {
        return serviceAddress;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source field: webservice_server_info.master")
    public Integer getMaster() {
        return master;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-01-18T17:36:21.207+01:00", comments="Source field: webservice_server_info.update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table webservice_server_info
     *
     * @mbg.generated Tue Jan 18 17:36:21 CET 2022
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uuid=").append(uuid);
        sb.append(", serviceAddress=").append(serviceAddress);
        sb.append(", master=").append(master);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table webservice_server_info
     *
     * @mbg.generated Tue Jan 18 17:36:21 CET 2022
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
        WebserviceServerInfoRecord other = (WebserviceServerInfoRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getServiceAddress() == null ? other.getServiceAddress() == null : this.getServiceAddress().equals(other.getServiceAddress()))
            && (this.getMaster() == null ? other.getMaster() == null : this.getMaster().equals(other.getMaster()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table webservice_server_info
     *
     * @mbg.generated Tue Jan 18 17:36:21 CET 2022
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getServiceAddress() == null) ? 0 : getServiceAddress().hashCode());
        result = prime * result + ((getMaster() == null) ? 0 : getMaster().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}