package thicuoiki2.phannhattan.com.nexus.store.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_banks")
public class PaymentBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "bank_account", nullable = false)
    private String bankAccount;

    @Column(name = "bank_owner", nullable = false)
    private String bankOwner;

    @Column(name = "bank_branch")
    private String bankBranch;

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }

    public String getBankOwner() { return bankOwner; }
    public void setBankOwner(String bankOwner) { this.bankOwner = bankOwner; }

    public String getBankBranch() { return bankBranch; }
    public void setBankBranch(String bankBranch) { this.bankBranch = bankBranch; }
}