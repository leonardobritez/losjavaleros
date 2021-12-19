package javaleros.frba.javaleros.security;

import org.passay.*;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;

import java.io.FileReader;
import java.io.IOException;


public class PasswordValidatorSingleton
{
    private static PasswordValidatorSingleton instance = null;

    public PasswordValidator passwordValidator;

    private PasswordValidatorSingleton()  {
        try {
            DictionaryRule rule = new DictionaryRule(
                    new WordListDictionary(WordLists.createFromReader(
                            // Reader around the word list file
                            new FileReader[] {new FileReader("password.txt")},
                            // True for case sensitivity, false otherwise
                            false,
                            // Dictionaries must be sorted
                            new ArraysSort())));
            passwordValidator = new PasswordValidator(
                    new LengthRule(8,64),
                    rule,
                    new UsernameRule(),
                    new CharacterCharacteristicsRule(3,
                            new CharacterRule(EnglishCharacterData.UpperCase, 1),
                            new CharacterRule(EnglishCharacterData.Digit, 1),
                            new CharacterRule(EnglishCharacterData.Special,1)));
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    public static PasswordValidatorSingleton getInstance()
    {
        if (instance == null)
            instance = new PasswordValidatorSingleton();

        return instance;
    }

    public RuleResult validate(String username, String password){
        var passwordData = new PasswordData(password);
        passwordData.setUsername(username);
        return passwordValidator.validate(passwordData);
    }
}