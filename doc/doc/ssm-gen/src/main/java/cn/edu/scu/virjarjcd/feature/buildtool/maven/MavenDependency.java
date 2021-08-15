package cn.edu.scu.virjarjcd.feature.buildtool.maven;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by virjar on 16/3/31.
 * 在版本属性声明，版本管理，依赖声明等各种场景为一个指定的依赖项提供数据
 */

public class MavenDependency implements Comparable<MavenDependency>{
    @Getter
    private Set<MavenDependency> exclusions = new HashSet<>();

    @Getter
    private String groupid;
    @Getter
    private String arctfictid;

    @Getter
    private String version;
    @Getter
    private String scope;

    private boolean forceSetVersion = false;

    private String versionKey;

    private boolean onlySetVersion = false;

    public boolean isForceSetVersion(){
        return forceSetVersion;
    }

    public MavenDependency forceVersion(boolean forceSetVersion){
        this.forceSetVersion = forceSetVersion;
        return this;
    }

    public MavenDependency addEceculison(String groupid,String arctfictid){
        exclusions.add(new MavenDependency(groupid,arctfictid,null));
        return this;
    }

    public MavenDependency(String groupid,String arctfictid,String verision){
        this.groupid = groupid;
        this.arctfictid = arctfictid;
        this.version = verision;
    }

    public boolean isOnlySetVersion() {
        return onlySetVersion;
    }

    public MavenDependency setOnlySetVersion(boolean onlySetVersion) {
        this.onlySetVersion = onlySetVersion;
        return this;
    }

    @Override
    public int compareTo(MavenDependency o) {
        int groupOrder = groupid.compareTo(o.groupid);
        return 0==groupOrder?arctfictid.compareTo(o.arctfictid):groupOrder;
    }


    public MavenDependency setVersion(String version){
        this.version = version;
        return this;
    }

    public MavenDependency setScope(String scope){
        this.scope = scope;
        return this;
    }

    public MavenDependency setVersionKey(String versionKey){
        this.versionKey = versionKey;
        return this;
    }

    public String getVersionKey(){
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isBlank(this.versionKey)){
            sb.append(this.groupid);
            sb.append(".");
            sb.append(this.arctfictid);
        }else{
            sb.append(this.versionKey);
        }
        sb.append(".version");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MavenDependency that = (MavenDependency) o;

        if (groupid != null ? !groupid.equals(that.groupid) : that.groupid != null) return false;
        return !(arctfictid != null ? !arctfictid.equals(that.arctfictid) : that.arctfictid != null);

    }

    @Override
    public int hashCode() {
        int result = groupid != null ? groupid.hashCode() : 0;
        result = 31 * result + (arctfictid != null ? arctfictid.hashCode() : 0);
        return result;
    }
}
