package cucumberJava;

import Service.UserService;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import java.util.Scanner;

public class CreatingAnAccount {


    String email;
    String pass;

    @Given("^I do not have an account and want to create one$")
    public void I_do_not_have_an_account_and_want_to_create_one() {

    }

    @When("^I fill in my information into the registration page$")
    public void I_fill_in_my_information_into_the_registration_page() {
        Scanner input = new Scanner(System.in);
        email = "j@j.nl";
        pass = "testattest";
    }

    @Then("^I will create an account$")
    public void I_will_create_an_account() {
        Scanner input = new Scanner(System.in);
        UserService userService = new UserService();
      //  userService.create(new User("Jan", new Account(email, pass)));
    }

}
