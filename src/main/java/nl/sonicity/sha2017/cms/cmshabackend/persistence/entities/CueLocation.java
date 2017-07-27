/**
 * Copyright © 2017 Sonicity (info@sonicity.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.sonicity.sha2017.cms.cmshabackend.persistence.entities;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by htrippaers on 21/07/2017.
 */
@Entity
@IdClass(CueLocation.CueLocationPk.class)
public class CueLocation implements Serializable, Persistable<CueLocation.CueLocationPk> {

    @Id
    @Column(nullable = false, name = "groupname")
    private String group;

    @Id
    @Column(nullable = false)
    private Integer page;

    @Id
    @Column(nullable = false)
    private Integer pageIndex;

    @Column(nullable = false)
    private Boolean reserved;

    @OneToOne(cascade = CascadeType.DETACH)
    private ActiveClaim activeClaim;

    @Transient
    private boolean newObject = false;

    protected CueLocation() {
    }

    public CueLocation(String group, Integer page, Integer pageIndex, Boolean reserved, ActiveClaim activeClaim) {
        this.group = group;
        this.page = page;
        this.pageIndex = pageIndex;
        this.reserved = reserved;
        this.activeClaim = activeClaim;
    }

    @Override
    public CueLocationPk getId() {
        return new CueLocationPk(group, page, pageIndex);
    }

    public String getGroup() {
        return group;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public ActiveClaim getActiveClaim() {
        return activeClaim;
    }

    public void setActiveClaim(ActiveClaim activeClaim) {
        this.activeClaim = activeClaim;
    }

    @Override
    public boolean isNew() {
        return newObject;
    }

    public void setNew(boolean newObject) {
        this.newObject = newObject;
    }

    @Override
    public String toString() {
        return "CueLocation{" +
                "group='" + group + '\'' +
                ", page=" + page +
                ", pageIndex=" + pageIndex +
                ", reserved=" + reserved +
                ", activeClaim=" + activeClaim +
                '}';
    }

    public static class CueLocationPk implements Serializable {
        private String group;
        private Integer page;
        private Integer pageIndex;

        protected CueLocationPk() {
        }

        public CueLocationPk(String group, Integer page, Integer pageIndex) {
            this.group = group;
            this.page = page;
            this.pageIndex = pageIndex;
        }

        public String getGroup() {
            return group;
        }

        public Integer getPage() {
            return page;
        }

        public Integer getPageIndex() {
            return pageIndex;
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
            return pageIndex.equals(that.pageIndex);
        }

        @Override
        public int hashCode() {
            int result = group.hashCode();
            result = 31 * result + page.hashCode();
            result = 31 * result + pageIndex.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "CueLocationPk{" +
                    "group='" + group + '\'' +
                    ", page=" + page +
                    ", pageIndex=" + pageIndex +
                    '}';
        }
    }
}
