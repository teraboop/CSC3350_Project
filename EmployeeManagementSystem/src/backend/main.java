package backend;

public class main {
    /**
     * Entry point for manual testing of backend components.
     *
     * @param args command-line arguments (not used)
     * @throws Exception if any backend operation fails
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Reports test = new Reports();
        Employee testemp = new Employee(1,1, "Gerald", "Testingson");
        System.out.println(test.getPaymentInfo(testemp));
        System.out.println(testemp.getEmpID());
        System.out.println(test.getMonthlyPayTitle(1, 12, 2025, testemp));
        System.out.println(test.getMonthlyPayDiv(1,12,2025,testemp));
        System.out.println(test.newHires(7, 2022, 10, 2022,testemp));
        //disregard this stuff it's just for testing
    }
}
