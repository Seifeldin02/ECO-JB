package model;

import javax.persistence.*;

@Entity
@Table(name = "institutions")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dunsNumber;

    @Column(nullable = false)
    private String institutionInfo;

    @Column(nullable = false, unique = true)
    private String adminEmail;

    @Column(nullable = false)
    private String adminPassword; // This should be encrypted

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDunsNumber() {
        return dunsNumber;
    }

    public void setDunsNumber(String dunsNumber) {
        this.dunsNumber = dunsNumber;
    }

    public String getInstitutionInfo() {
        return institutionInfo;
    }

    public void setInstitutionInfo(String institutionInfo) {
        this.institutionInfo = institutionInfo;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}