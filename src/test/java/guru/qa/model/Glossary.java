package guru.qa.model;

public class Glossary {
    public String id;
    public String name;
    public String telephone;
    public String[] pets;
    public Address address;

    public static class Address{
        public String street;
        public String town;
        public String postcode;
    }
}
