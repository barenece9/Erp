package com.lnsel.erp.settergetter;

/**
 * Created by db on 4/17/2017.
 */
public class UserInfo {
    public String userId="";
    public String userName="";
    public String userType="";
    public String reporting_emp_id="";
    public String payroll_mstr_holiday_category_id="";

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public String fin_year_identifier="";
    public Boolean login=false;

    public String getReporting_person() {
        return reporting_person;
    }

    public void setReporting_person(String reporting_person) {
        this.reporting_person = reporting_person;
    }

    public String reporting_person="";

    public String getFin_year_identifier() {
        return fin_year_identifier;
    }

    public void setFin_year_identifier(String fin_year_identifier) {
        this.fin_year_identifier = fin_year_identifier;
    }

    public String getReporting_emp_id() {
        return reporting_emp_id;
    }

    public void setReporting_emp_id(String reporting_emp_id) {
        this.reporting_emp_id = reporting_emp_id;
    }

    public String getPayroll_mstr_holiday_category_id() {
        return payroll_mstr_holiday_category_id;
    }

    public void setPayroll_mstr_holiday_category_id(String payroll_mstr_holiday_category_id) {
        this.payroll_mstr_holiday_category_id = payroll_mstr_holiday_category_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


}
