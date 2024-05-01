package bullscows;

import java.util.*;

class SecretCode{
    private ArrayList<String> secretCode;
    private Set<String> uniqueItems;

    public SecretCode() throws Exception {
        String[] str = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        this.secretCode=new ArrayList<>();
        createSecretCode(str, 4);
    }

    public SecretCode(int size) throws Exception {
        String[] str = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        this.secretCode=new ArrayList<>();
        createSecretCode(str, size);
    }

    public SecretCode(int size, String items) throws Exception {
        String[] str = items.split("");
        this.secretCode=new ArrayList<>();
        createSecretCode(str, size);
    }

    public void createSecretCode(String[] str, int size) throws Exception {
        createUniqueItems(str);
        if(this.uniqueItems.size() < size){
            String error = String.format("Error: it's not possible to generate a code with a length of %d with %d unique symbols.",
                    size, this.uniqueItems.size());

            throw new Exception(error);
        }
        createSecretCode(size);
        System.out.printf("The secret is prepared: %s %s.\n", this.getMaskedCode(), this.getUsedDigits());
    }

    void createUniqueItems(String[] items) {
        this.uniqueItems = new HashSet<String>(Arrays.asList(items));
    }

    private void createSecretCode(int size){
        Random random = new Random();
        List<String> itemList = new ArrayList<>(this.uniqueItems);
        for (int i = 0; i < size; i++) {
            String digit;
            int randomIndex;
            do {
                randomIndex = random.nextInt(itemList.size());
                digit = itemList.get(randomIndex);
            }while((digit.equals("0") && i == 0));
            this.secretCode.add(digit);
            itemList.remove(randomIndex);
        }
    }

    public String convertToString(){
        String str = "";
        for(String digit: this.secretCode){
            str += digit;
        }
        return str;
    }

    public String getMaskedCode(){
        String str = "*".repeat(this.secretCode.size());
        return str;
    }

    public String getUsedDigits(){
        List<String> list = new ArrayList<>(this.uniqueItems);
        Collections.sort(list);
        String str = "0-9";
        if(list.size() > 10){
            str = String.format("(%s, a-%s)", str, list.get(list.size() - 1));
        }
        return str;
    }

    public boolean checkSecretCode(String item) {
        String[] digit = item.split("");
        int bulls = 0;
        int cows = 0;
        for (int i = 0; i < this.secretCode.size(); i++) {
            if(digit[i].equals(this.secretCode.get(i))){
                bulls++;
            }else if (this.secretCode.contains(digit[i])) {
                cows++;
            }
        }
        if(bulls > 0 && cows > 0) {
            System.out.printf("Grade: %d bull(s) and %d cow(s).\n", bulls, cows);
        }else if(bulls > 0){
            System.out.printf("Grade: %d bull(s).\n", bulls);
        } else if(cows > 0){
            System.out.printf("Grade: %d cows(s).\n", cows);
        }else{
            System.out.print("Grade: None.\n");
        }
        if(bulls != this.secretCode.size()) return false;
        return true;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Input the length of the secret code:");
            String sSize = scanner.nextLine();
            int size;
            try{
                size = Integer.parseInt(sSize);
                if(size <= 0){
                    throw new Exception("Error: \"0\" isn't a valid number.");
                }
            }catch(NumberFormatException e){
                String str = String.format("Error: \"%s\" isn't a valid number.", sSize);
                throw new Exception(str);
            }
            System.out.println("Input the number of possible symbols in the code:");
            int length = scanner.nextInt();
            scanner.nextLine();
            SecretCode secretCode;
            String str = new String("0123456789abcdefghijklmnopqrstuvwxyz");
            if(length > str.length()){
                String xyz = String.format("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                throw new Exception(xyz);
            }
            secretCode = new SecretCode(size, str.substring(0, length));
            secretCode.getUsedDigits();
            System.out.println("Okay, let's start a game!");
            String input;
            int turn = 1;
            do{
                System.out.printf("Turn %d:\n", turn++);
                input = scanner.nextLine();

            }while(!secretCode.checkSecretCode(input));
            System.out.println("Congratulations! You guessed the secret code.");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        scanner.close();
    }
}
