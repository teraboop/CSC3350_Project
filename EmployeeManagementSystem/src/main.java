public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Reports test = new Reports();
        Employee testemp = new Employee(0,1);
        System.out.println(test.getPaymentInfo(testemp));
        System.out.println(testemp.getEmpID());
        System.out.println(test.getMonthlyPayTitle(102, 12, 2025));
        System.out.println(test.getMonthlyPayDiv(999,12,2025));
        System.out.println(test.newHires(7, 2022, 10, 2022));
        //disregard this stuff it's just for testing
    }
}
