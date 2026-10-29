import java.util.Scanner;
class Employee
{
 int age;
 float bp,nbp,m;
 void calculate()
 {
 if(age>56)
  {
    m=2*bp/10;
    nbp=bp+m;
  }
  else if(age>46)
  {
    m=15*bp/100;
    nbp=bp+m;
  }
  else
  {
    m=bp/10;
    nbp=bp+m;
  }
 }
  void display()
  {
    System.out.println("The new basic pay is: "+nbp);
  }
 }
class Main
{
    public static void main(String args[])
    {
        Scanner in=new Scanner(System.in);
        Employee e=new Employee();
        System.out.println("Enter the age of employee: ");
        e.age=in.nextInt();
        System.out.println("Enter the basic pay of employee: ");
        e.bp=in.nextFloat();
        e.calculate();
        e.display();
    }
}