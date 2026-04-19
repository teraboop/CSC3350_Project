import java.sql.Date;
public class PayrollRecord {
    public Date payDate;
    public double earnings;
    public double fedTax;
    public double fedMed;
    public double fedSS;
    public double stateTax;
    public double retirement401k;
    public double healthcare;
    
    // getters/setters
    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public double getFedTax() {
        return fedTax;
    }

    public void setFedTax(double fedTax) {
        this.fedTax = fedTax;
    }

    public double getFedMed() {
        return fedMed;
    }

    public void setFedMed(double fedMed) {
        this.fedMed = fedMed;
    }

    public double getFedSS() {
        return fedSS;
    }

    public void setFedSS(double fedSS) {
        this.fedSS = fedSS;
    }

    public double getStateTax() {
        return stateTax;
    }

    public void setStateTax(double stateTax) {
        this.stateTax = stateTax;
    }

    public double getRetirement401k() {
        return retirement401k;
    }

    public void setRetirement401k(double retirement401k) {
        this.retirement401k = retirement401k;
    }

    public double getHealthcare() {
        return healthcare;
    }

    public void setHealthcare(double healthcare) {
        this.healthcare = healthcare;
    }
}
