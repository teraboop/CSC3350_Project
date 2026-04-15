public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Reports test = new Reports();
        Employee testemp = new Employee(0,1);
        System.out.println(test.getPaymentInfo(testemp));
        System.out.println(testemp.getEmpID());
    }
}
