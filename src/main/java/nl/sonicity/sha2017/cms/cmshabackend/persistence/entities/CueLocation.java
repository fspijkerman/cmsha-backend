package nl.sonicity.sha2017.cms.cmshabackend.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

/**
 * Created by htrippaers on 21/07/2017.
 */
@Entity
@IdClass(CueLocation.CueLocationPk.class)
public class CueLocation implements Serializable {

    @Id
    @Column(nullable = false, name = "groupname")
    private String group;

    @Id
    @Column(nullable = false)
    private Integer page;

    @Id
    @Column(nullable = false)
    private Integer index;

    private Integer linkedTitanId;

    protected CueLocation() {
    }

    public CueLocation(String group, Integer page, Integer index, Integer linkedTitanId) {
        this.group = group;
        this.page = page;
        this.index = index;
        this.linkedTitanId = linkedTitanId;
    }

    public String getGroup() {
        return group;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getLinkedTitanId() {
        return linkedTitanId;
    }

    public static class CueLocationPk implements Serializable {
        private String group;
        private Integer page;
        private Integer index;

        protected CueLocationPk() {
        }

        public CueLocationPk(String group, Integer page, Integer index) {
            this.group = group;
            this.page = page;
            this.index = index;
        }

        public String getGroup() {
            return group;
        }

        public Integer getPage() {
            return page;
        }

        public Integer getIndex() {
            return index;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            CueLocationPk that = (CueLocationPk)o;

            if (!group.equals(that.group)) {
                return false;
            }
            if (!page.equals(that.page)) {
                return false;
            }
            return index.equals(that.index);
        }

        @Override
        public int hashCode() {
            int result = group.hashCode();
            result = 31 * result + page.hashCode();
            result = 31 * result + index.hashCode();
            return result;
        }
    }
}
