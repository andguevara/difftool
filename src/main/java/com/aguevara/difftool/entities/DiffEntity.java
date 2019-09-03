package com.aguevara.difftool.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "diffstore")
public class DiffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "diffid")
    private Long diffid;

    @Column(name = "diffleft")
    private String diffleft;

    @Column(name = "diffright")
    private String diffright;

    @Column(name = "result")
    private String result;

    public DiffEntity() {}

    public void setDiffid(Long diffid) {
        this.diffid = diffid;
    }

    public Long getDiffid() {
        return this.diffid;
    }

    public void setDiffleft(String diffleft) {
        this.diffleft = diffleft;
    }

    public void setDiffright(String diffright) {
        this.diffright = diffright;
    }

    public String getDiffleft() {
        return diffleft;
    }

    public String getDiffright() {
        return diffright;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiffEntity diffEntity = (DiffEntity)o;
        if (!diffEntity.getDiffid().equals(this.diffid)) {
            return false;
        }
        if (diffEntity.getDiffright() != null && diffEntity.getDiffleft() != null) {
            return diffEntity.getDiffleft().equals(this.diffleft)
                    && diffEntity.getDiffright().equals(this.diffright);
        }
        if (diffEntity.getDiffleft() == null && diffEntity.getDiffright() == null) {
            return true;
        }
        if (diffEntity.getDiffleft() == null) {
            return diffEntity.getDiffright().equals(this.diffright);
        }
        if(diffEntity.getDiffright() == null) {
            return diffEntity.getDiffleft().equals(this.diffleft);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 + Objects.hash(diffid) + Objects.hash(diffleft) + Objects.hashCode(diffright);
    }
}
