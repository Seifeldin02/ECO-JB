package model;
import javax.persistence.*;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "carbon")
public class Carbon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @OneToOne
    @JoinColumn(name = "institution_id")
    
    
    private Institution institution;

    private String waterDate;

    private String electricDate;
    
    private String recycleDate;
    
    private Double waterUsage;
    private Double waterBill;
    private Integer waterDays;

    private Double electricUsage;
    private Double electricBill;
    private Integer electricDays;

    private Double recycleUsage;
    private Double recycleBill;
    private Integer recycleDays;

    // Carbon factors
    private static final Double WATER_FACTOR = 0.419;
    private static final Double ELECTRIC_FACTOR = 0.584;
    private static final Double RECYCLE_FACTOR = 2.86;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Double getWaterUsage() {
        return waterUsage;
    }

    public void setWaterUsage(Double waterUsage) {
        this.waterUsage = waterUsage;
    }

    public Double getWaterBill() {
        return waterBill;
    }

    public void setWaterBill(Double waterBill) {
        this.waterBill = waterBill;
    }

    public String getWaterDate() {
        return waterDate;
    }

    public void setWaterDate(String waterDate) {
        this.waterDate = waterDate;
    }

    public Integer getWaterDays() {
        return waterDays;
    }

    public void setWaterDays(Integer waterDays) {
        this.waterDays = waterDays;
    }

    public Double getElectricUsage() {
        return electricUsage;
    }

    public void setElectricUsage(Double electricUsage) {
        this.electricUsage = electricUsage;
    }

    public Double getElectricBill() {
        return electricBill;
    }

    public void setElectricBill(Double electricBill) {
        this.electricBill = electricBill;
    }

    public String getElectricDate() {
        return electricDate;
    }

    public void setElectricDate(String electricDate) {
        this.electricDate = electricDate;
    }

    public Integer getElectricDays() {
        return electricDays;
    }

    public void setElectricDays(Integer electricDays) {
        this.electricDays = electricDays;
    }

    public Double getRecycleUsage() {
        return recycleUsage;
    }

    public void setRecycleUsage(Double recycleUsage) {
        this.recycleUsage = recycleUsage;
    }

    public Double getRecycleBill() {
        return recycleBill;
    }

    public void setRecycleBill(Double recycleBill) {
        this.recycleBill = recycleBill;
    }

    public String getRecycleDate() {
        return recycleDate;
    }

    public void setRecycleDate(String recycleDate) {
        this.recycleDate = recycleDate;
    }

    public Integer getRecycleDays() {
        return recycleDays;
    }

    public void setRecycleDays(Integer recycleDays) {
        this.recycleDays = recycleDays;
    }

    public Double calculateWaterCarbonFootprint() {
        return this.waterUsage * WATER_FACTOR;
    }

    public Double calculateElectricCarbonFootprint() {
        return this.electricUsage * ELECTRIC_FACTOR;
    }

    public Double calculateRecycleCarbonFootprint() {
        return this.recycleUsage * RECYCLE_FACTOR;
    }
}