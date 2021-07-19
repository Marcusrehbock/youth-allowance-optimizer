import java.util.Scanner;

public class Calculator {
    private static final double RENT_ASSISTANCE = 93.87;
    private static final int ENERGY_SUPPLEMENT = 7;
    private static final int AVERAGE_GROCERY_COST_PER_WEEK = 107;
    private static final int COST_OF_UTILITIES_PER_WEEK = 45;

    public static double calcFortCLPay(double wage, double rent) {
        double result = 0.0;
        if (wage < 437) {
            result = 512.5;
        } else if (wage > 437 && wage < 524) {
            result = 512.5 - (.5 * (wage - 437));
        } else if (wage > 524 && wage < 1317.34) {
            result = 512.5 - (43.5 + (.6 * (wage - 524)));
        } else if (wage > 1317.34) {
            result = 0.00;
        }
        //Rent has to be above $126 a week to get Max rent assistance
        if (rent * 2 < 250.96) {
            return result + ENERGY_SUPPLEMENT;
        }
        return result + RENT_ASSISTANCE + ENERGY_SUPPLEMENT;
    }

    public static double leftOverHelper(int hours, double hourlyWage, double rent) {
        double fortnightlyIncome = hours * hourlyWage * 2;
        double centreLinkPayment = calcFortCLPay(fortnightlyIncome, rent);
        double combinedIncome = fortnightlyIncome + centreLinkPayment;
        return calcLeftOverMoney(combinedIncome, rent);

    }

    public static double calcEffectiveHourlyRate(int hours, double leftOver, double rent) {
        return (leftOver + (2 * (rent + COST_OF_UTILITIES_PER_WEEK + AVERAGE_GROCERY_COST_PER_WEEK)))/(hours * 2);
    }

    public static int calcRequiredHoursWork(double takeHomePay, double hourlyWage, double rent) {
        int maxHours = Integer.MAX_VALUE;

        for (int hours = 0; hours < maxHours; hours++) {
            double leftover = leftOverHelper(hours, hourlyWage, rent);
            if (leftover >= takeHomePay) {
                return hours;
            }
        }
        return maxHours;
    }

    public static double calcLeftOverMoney(double combinedIncome, double rent) {

        return combinedIncome - (2 * (rent + COST_OF_UTILITIES_PER_WEEK + AVERAGE_GROCERY_COST_PER_WEEK));

    }

    public static void main(String[] args) {

        try {

            System.out.println("Welcome! This calculator allows you to calculate " +
                    "your CentreLink payment given your fortnightly income!" +
                    "\nIt even includes an optimisation tool to make sure you don't work too many hours!\n");
            //Enter Wage Fortnightly
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Your Fortnightly Income: ");
            double wage = Double.parseDouble(scanner.nextLine());
            System.out.println("Enter Your Weekly Rent: ");
            double rent = Double.parseDouble(scanner.nextLine());

            double centreLink = calcFortCLPay(wage, rent);
            double combinedIncome = wage + centreLink;
            double leftOver = calcLeftOverMoney(combinedIncome, rent);

            System.out.println("Your Fortnightly CentreLink Pay is: $" + centreLink + " or $" + centreLink/2 + " per week."+"\n");
            System.out.println("Your Total Fortnightly Income is: $" + combinedIncome + "\n");

            System.out.println("Minus Rent, Groceries & Utilities you can expect to take home around: $" + leftOver);
            System.out.println("Would you like to optimise how many hours you work per week (Y/N): ");

            String continuation = String.valueOf(scanner.nextLine());

            if (continuation.equals("N") || continuation.equals("n")) {
                return;
            } else if (continuation.equals("Y") || continuation.equals("y")) {

                System.out.println("What is your required take home pay per Fortnight?: ");
                double takeHomePay = Double.parseDouble(scanner.nextLine());
                System.out.println("What is your hourly wage?: ");
                double hourlyWage = Double.parseDouble(scanner.nextLine());
                //magic number sidesteps rent parameter
                int hours = calcRequiredHoursWork(takeHomePay, hourlyWage, rent);

                int leftOvers = (int) leftOverHelper(hours, hourlyWage, rent);

                System.out.println("You should work " + hours + " hours per week to take home $" + leftOvers + " per fortnight.");

                System.out.println("Effective hourly rate: $"
                        + calcEffectiveHourlyRate(hours, leftOvers, rent) + " per hour");
            }

        } catch (NumberFormatException e) {
            System.out.println("Your input was of the wrong format");
        }





    }




}
