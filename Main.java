import java.util.*;

/**
* 
* A simple algorithm that generates all the (ordered) possible String's from a given alphabet and the given intervall
* until the given text is generated.
* Example use case: Brute forcing passwords 
* 
* @author Ege Girit
* @version 1.0
*/

public class Main {

    static String alphabet = "";    // All the possible characters that the ciphertext can contain
    static int alphabetLength = 0;  // Length of the alphabet
    static String cipherText = "";  // ciphertext that we are going to find with brute force
    
    // Eliminates the duplicate characters in the given input String
    public static String eliminateDuplicate(String input){

        String result = "";
        for (int i = 0; i < input.length(); i++) {
        	// Only append the unique characters to the result
            if(!result.contains(String.valueOf(input.charAt(i)))) {
                result += String.valueOf(input.charAt(i));
            }
        }

        return result;

    }

    // Sort a string array alphabetically
    public static String sortString(String inputString){

        // convert input string to char array
        char tempArray[] = inputString.toCharArray();

        // sort tempArray
        Arrays.sort(tempArray);

        // return new sorted string
        return new String(tempArray);

    }

    // Checks if the ciphertext contains different letters than the alphabet
    public static boolean checkForDifferentLetter(String cipherTextInput, String alphabetInput){

        for(int i = 0; i < cipherTextInput.length(); i++){

            for(int j = 0; j < alphabetInput.length(); j++){

                if( !alphabetInput.contains(cipherTextInput.charAt(i) + "") ){
                    return false;
                }

            }

        }

        return true;

    }

    // Returns the next letter in the alphabet
    public static String incrementLetter(String letter){

        if( !alphabet.contains(letter + "") ){
            System.out.println("Letter is not in the alphabet!");
            System.exit(1);
        }

        int indexOfLetter = alphabet.indexOf(letter);

        if( (indexOfLetter + 1) >= alphabetLength ){
            indexOfLetter = 0;
        }else{
            ++indexOfLetter;
        }

        return String.valueOf(alphabet.charAt(indexOfLetter));

    }

    // Returns the next possible string
    public static String incrementString(String str){

        int strLength = str.length();

        // Initialize Bruteforce String 
        if( strLength == 0 ){
            return String.valueOf(Main.alphabet.charAt(0));
        }

        // We will add 1 to the last element
        String lastLetter = String.valueOf(str.charAt(strLength-1));

        // Check if addition will cause overflow, if yes check how many digits will be effected
        if( lastLetter.compareTo(String.valueOf(Main.alphabet.charAt( Main.alphabetLength-1 ))) == 0 ){

            // Check the string from right to left to see how many digits will be effected after addition
            int effectedDigits;  // decreases from strLength-1
            int effectedDigitsFromZero = 0;  // increases

            for( effectedDigits = strLength-1; effectedDigits >= 0; effectedDigits-- ){

                // Check if the digit we are looking at is the last possible digit in the alphabet and will be changed after addition
                if( str.charAt(effectedDigits) == ( Main.alphabet.charAt( Main.alphabetLength-1 )) ){
                    
                    ++effectedDigitsFromZero;
                }
                // if not equals stop counting
                else {
                    break;
                }
            }

            // All possibilities for this length is completed, time to increase the length of the string by 1
            if(effectedDigitsFromZero == strLength){
                System.out.println( "Increasing Length from " + strLength + " to " + (strLength+1) );
                // Take the first letter of the alphabet and make a new string with increased length by 1
                String newBruteforce = "";
                for(int i = 0; i <= strLength; i++){
                    newBruteforce += Main.alphabet.charAt(0);
                }

                return newBruteforce;

            }
            else {

                // Increment the last digit at left that is not the last letter of the alphabet, örn: 199 + 1 = 299
                int indexOfLeft = (strLength-1) - effectedDigitsFromZero;
                String incrementedDigit = incrementLetter( String.valueOf(str.charAt(indexOfLeft)) );

                // Transform the overflow digits to the first letter of the alphabet, 299 -> 200
                String transform = replaceLetterAtIndex(str, incrementedDigit, indexOfLeft);

                // Reset the overflow bits to the first letter of alphabet
                int x = 0;
                for(int i = effectedDigitsFromZero; i > 0; i--){
                    transform = replaceLetterAtIndex( transform, String.valueOf(Main.alphabet.charAt(0)), (str.length() - 1) - x);
                    x++;
                }

                return transform;

            }

        }
        else {
            // increment the last letter
            String incrementedLetter = Main.incrementLetter(lastLetter);

            // replace the incremented last letter with the last letter of string
            return replaceLetterAtIndex(str, incrementedLetter,str.length()-1);
        }

    }

    // Used in incrementString to change the added string
    public static String replaceLetterAtIndex(String original, String letter, int index){

        if(index < 0 || index >= original.length()){
            System.out.println("Index is out of bounds of original string: " + index + " ," + original);
            System.exit(1);
        }

        char[] chars = original.toCharArray();
        chars[ index ] = letter.charAt(0);
        return String.valueOf(chars);

    }

    // Initializes the brute force string with the given length
    public static String initBruteForceVariable( int startLength ){

        String bruteforce = "";

        for(int i = 0; i < startLength; i++){
            bruteforce += Main.alphabet.charAt(0);
        }

        return bruteforce;

    }

    // A program that constructs all the possible combinations from the given alphabet, and compares it with ciphertext
    // Length interval is given, standard parameters are startLength = 0 (inclusive), stopLength = ciphertext.length() (exclusive)
    public static void bruteForce(String alphabetInput, String cipherTextInput, int startLength, int stopLength){
    	
        if( (alphabetInput.length() == 0) || (cipherTextInput.length() == 0) ){

            System.out.println("Empty Input!");
            System.exit(1);

        }
        
        if( stopLength < cipherTextInput.length() ){
        	System.out.println("Ciphertext is longer than the given intervall!");
        	System.out.println("Ciphertext: " + cipherTextInput);
        	System.out.println("stopLength: " + stopLength);
            System.exit(1);
        }

        Main.cipherText = cipherTextInput;

        System.out.println("Alphabet   Input: " + alphabetInput);
        System.out.println("Ciphertext Input: " + Main.cipherText);

        String duplicateEliminated = eliminateDuplicate(alphabetInput);

        String sortedAlphabet = sortString(duplicateEliminated);

        Main.alphabet = sortedAlphabet;

        Main.alphabetLength = sortedAlphabet.length();

        if( !checkForDifferentLetter(Main.cipherText, sortedAlphabet) ){
            System.out.println("Cyphertext contains different letters from alphabet!");
            System.exit(1);
        }

        System.out.println("Sorted Alphabet: " + sortedAlphabet + " (length: " + Main.alphabetLength + ")");

        System.out.println("Starting brute force with length [" + startLength + "," + stopLength + ")");

        String bruteForce = "";
        long count = 0;

        bruteForce = initBruteForceVariable(startLength);

        while( (!bruteForce.equals(Main.cipherText)) && (bruteForce.length() != stopLength) ){

            count++;
            System.out.println( count + ": " + bruteForce );
            bruteForce = Main.incrementString(bruteForce);

            // Uncomment this if you want to see the output slower
            /*
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */

        }
       
        System.out.println( "Ciphertext found: " + bruteForce);
        
    }

    public static void main(String[] args){

    	// bruteForce(String alphabet, String ciphertext, int startLength, int stopLength)
    	// startLength is inclusive, stopLength is exclusive
    	// Generate all possible Strings, until ddddd is generated.
    	bruteForce( "abcd", "ddddd", 1, 7 ); 
    }

}
