package com.github.zelmothedragon.dyna.customer;

import com.github.zelmothedragon.dyna.common.persistence.entity.AbstractEntity;
import javax.enterprise.context.Dependent;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.config.PropertyOrderStrategy;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Dependent
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
@Entity
@Table(name = "customer")
@Access(AccessType.FIELD)
public class Customer extends AbstractEntity {

    @NotBlank
    @JsonbProperty(value = "givenName", nillable = false)
    @Column(name = "given_name", nullable = false)
    private String givenName;

    @NotBlank
    @JsonbProperty(value = "familyName", nillable = false)
    @Column(name = "family_name", nullable = false)
    private String familyName;

    @NotBlank
    @Email
    @JsonbProperty(value = "email", nillable = false)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @JsonbProperty(value = "gender", nillable = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotBlank
    @JsonbProperty(value = "phoneNumber", nillable = false)
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotBlank
    @JsonbProperty(value = "rank", nillable = false)
    @Column(name = "rank", nullable = false)
    private String rank;

    @NotBlank
    @JsonbProperty(value = "shortRank", nillable = false)
    @Column(name = "short_rank", nullable = false)
    private String shortRank;

    public Customer() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer{id=");
        sb.append(id);
        sb.append(", version=");
        sb.append(version);
        sb.append(", givenName=");
        sb.append(givenName);
        sb.append(", familyName=");
        sb.append(familyName);
        sb.append(", email=");
        sb.append(email);
        sb.append(", gender=");
        sb.append(gender);
        sb.append(", phoneNumber=");
        sb.append(phoneNumber);
        sb.append(", rank=");
        sb.append(rank);
        sb.append(", shortRank=");
        sb.append(shortRank);
        sb.append('}');
        return sb.toString();
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getShortRank() {
        return shortRank;
    }

    public void setShortRank(String shortRank) {
        this.shortRank = shortRank;
    }

}
