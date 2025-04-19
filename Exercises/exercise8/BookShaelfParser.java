package Exercises.exercise8;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BookShaelfParser {
    // Model classes
    static class Author {
        private String name;
        
        public Author(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }

    static class Book {
        private String title;
        private int publishedYear;
        private int numberOfPages;
        private List<Author> authors;
        
        public Book(String title, int publishedYear, int numberOfPages) {
            this.title = title;
            this.publishedYear = publishedYear;
            this.numberOfPages = numberOfPages;
            this.authors = new ArrayList<>();
        }
        
        public void addAuthor(Author author) {
            authors.add(author);
        }
        
        public String getTitle() {
            return title;
        }
        
        public int getPublishedYear() {
            return publishedYear;
        }
        
        public int getNumberOfPages() {
            return numberOfPages;
        }
        
        public List<Author> getAuthors() {
            return authors;
        }
    }
    
    static class BookShelf {
        private List<Book> books;
        
        public BookShelf() {
            books = new ArrayList<>();
        }
        
        public void addBook(Book book) {
            books.add(book);
        }
        
        public List<Book> getBooks() {
            return books;
        }
    }

    // XML Parser handling methods
    public static void createSampleXmlFile() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            // Root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("BookShelf");
            doc.appendChild(rootElement);
            
            // Book 1
            Element book1 = doc.createElement("Book");
            rootElement.appendChild(book1);
            
            Element title1 = doc.createElement("title");
            title1.setTextContent("Clean Code");
            book1.appendChild(title1);
            
            Element publishedYear1 = doc.createElement("publishedYear");
            publishedYear1.setTextContent("2008");
            book1.appendChild(publishedYear1);
            
            Element numberOfPages1 = doc.createElement("numberOfPages");
            numberOfPages1.setTextContent("464");
            book1.appendChild(numberOfPages1);
            
            Element authors1 = doc.createElement("authors");
            book1.appendChild(authors1);
            
            Element author1 = doc.createElement("author");
            author1.setTextContent("Robert C. Martin");
            authors1.appendChild(author1);
            
            // Book 2
            Element book2 = doc.createElement("Book");
            rootElement.appendChild(book2);
            
            Element title2 = doc.createElement("title");
            title2.setTextContent("Effective Java");
            book2.appendChild(title2);
            
            Element publishedYear2 = doc.createElement("publishedYear");
            publishedYear2.setTextContent("2018");
            book2.appendChild(publishedYear2);
            
            Element numberOfPages2 = doc.createElement("numberOfPages");
            numberOfPages2.setTextContent("412");
            book2.appendChild(numberOfPages2);
            
            Element authors2 = doc.createElement("authors");
            book2.appendChild(authors2);
            
            Element author2 = doc.createElement("author");
            author2.setTextContent("Joshua Bloch");
            authors2.appendChild(author2);
            
            // Book 3
            Element book3 = doc.createElement("Book");
            rootElement.appendChild(book3);
            
            Element title3 = doc.createElement("title");
            title3.setTextContent("Design Patterns");
            book3.appendChild(title3);
            
            Element publishedYear3 = doc.createElement("publishedYear");
            publishedYear3.setTextContent("1994");
            book3.appendChild(publishedYear3);
            
            Element numberOfPages3 = doc.createElement("numberOfPages");
            numberOfPages3.setTextContent("395");
            book3.appendChild(numberOfPages3);
            
            Element authors3 = doc.createElement("authors");
            book3.appendChild(authors3);
            
            Element author3_1 = doc.createElement("author");
            author3_1.setTextContent("Erich Gamma");
            authors3.appendChild(author3_1);
            
            Element author3_2 = doc.createElement("author");
            author3_2.setTextContent("Richard Helm");
            authors3.appendChild(author3_2);
            
            Element author3_3 = doc.createElement("author");
            author3_3.setTextContent("Ralph Johnson");
            authors3.appendChild(author3_3);
            
            Element author3_4 = doc.createElement("author");
            author3_4.setTextContent("John Vlissides");
            authors3.appendChild(author3_4);
            
            // Write to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("bookshelf.xml"));
            transformer.transform(source, result);
            
            System.out.println("Sample XML file created successfully.");
            
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
    public static void parseXmlFile() {
        try {
            File xmlFile = new File("bookshelf.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            
            doc.getDocumentElement().normalize();
            
            NodeList bookList = doc.getElementsByTagName("Book");
            
            System.out.println("---- XML PARSING RESULTS ----");
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            System.out.println("Number of books: " + bookList.getLength());
            
            for (int i = 0; i < bookList.getLength(); i++) {
                Node bookNode = bookList.item(i);
                
                if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element bookElement = (Element) bookNode;
                    
                    String title = bookElement.getElementsByTagName("title").item(0).getTextContent();
                    String publishedYear = bookElement.getElementsByTagName("publishedYear").item(0).getTextContent();
                    String numberOfPages = bookElement.getElementsByTagName("numberOfPages").item(0).getTextContent();
                    
                    System.out.println("\nBook #" + (i + 1));
                    System.out.println("Title: " + title);
                    System.out.println("Published Year: " + publishedYear);
                    System.out.println("Number of Pages: " + numberOfPages);
                    
                    Element authorsElement = (Element) bookElement.getElementsByTagName("authors").item(0);
                    NodeList authorList = authorsElement.getElementsByTagName("author");
                    
                    System.out.println("Authors:");
                    for (int j = 0; j < authorList.getLength(); j++) {
                        Node authorNode = authorList.item(j);
                        System.out.println(authorNode.getTextContent());
                    }
                }
            }
            
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void addBookToXml(String title, int publishedYear, int numberOfPages, List<String> authors) {
        try {
            File xmlFile = new File("bookshelf.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            
            // Create new book element
            Element newBook = doc.createElement("Book");
            
            Element titleElement = doc.createElement("title");
            titleElement.setTextContent(title);
            newBook.appendChild(titleElement);
            
            Element publishedYearElement = doc.createElement("publishedYear");
            publishedYearElement.setTextContent(String.valueOf(publishedYear));
            newBook.appendChild(publishedYearElement);
            
            Element numberOfPagesElement = doc.createElement("numberOfPages");
            numberOfPagesElement.setTextContent(String.valueOf(numberOfPages));
            newBook.appendChild(numberOfPagesElement);
            
            Element authorsElement = doc.createElement("authors");
            newBook.appendChild(authorsElement);
            
            for (String authorName : authors) {
                Element authorElement = doc.createElement("author");
                authorElement.setTextContent(authorName);
                authorsElement.appendChild(authorElement);
            }
            
            // Add new book to the root element
            doc.getDocumentElement().appendChild(newBook);
            
            // Write back to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
            
            System.out.println("\nBook added to XML successfully.");
            
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
    
    // JSON handling methods
    public static void createSampleJsonFile() {
        try {
            JSONObject bookshelf = new JSONObject();
            JSONArray books = new JSONArray();
            
            // Book 1
            JSONObject book1 = new JSONObject();
            book1.put("title", "Clean Code");
            book1.put("publishedYear", 2008);
            book1.put("numberOfPages", 464);
            
            JSONArray authors1 = new JSONArray();
            authors1.add("Robert C. Martin");
            book1.put("authors", authors1);
            books.add(book1);
            
            // Book 2
            JSONObject book2 = new JSONObject();
            book2.put("title", "Effective Java");
            book2.put("publishedYear", 2018);
            book2.put("numberOfPages", 412);
            
            JSONArray authors2 = new JSONArray();
            authors2.add("Joshua Bloch");
            book2.put("authors", authors2);
            books.add(book2);
            
            // Book 3
            JSONObject book3 = new JSONObject();
            book3.put("title", "Design Patterns");
            book3.put("publishedYear", 1994);
            book3.put("numberOfPages", 395);
            
            JSONArray authors3 = new JSONArray();
            authors3.add("Erich Gamma");
            authors3.add("Richard Helm");
            authors3.add("Ralph Johnson");
            authors3.add("John Vlissides");
            book3.put("authors", authors3);
            books.add(book3);
            
            // Add books to bookshelf
            bookshelf.put("BookShelf", books);
            
            // Write to JSON file
            FileWriter file = new FileWriter("bookshelf.json");
            file.write(bookshelf.toJSONString());
            file.flush();
            file.close();
            
            System.out.println("Sample JSON file created successfully.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void parseJsonFile() {
        JSONParser parser = new JSONParser();
        
        try {
            Object obj = parser.parse(new FileReader("bookshelf.json"));
            JSONObject jsonObject = (JSONObject) obj;
            
            JSONArray books = (JSONArray) jsonObject.get("BookShelf");
            
            System.out.println("\n------- JSON PARSING RESULTS -------");
            System.out.println("Number of books: " + books.size());
            
            for (int i = 0; i < books.size(); i++) {
                JSONObject book = (JSONObject) books.get(i);
                
                String title = (String) book.get("title");
                long publishedYear = (long) book.get("publishedYear");
                long numberOfPages = (long) book.get("numberOfPages");
                
                System.out.println("\nBook #" + (i + 1));
                System.out.println("Title: " + title);
                System.out.println("Published Year: " + publishedYear);
                System.out.println("Number of Pages: " + numberOfPages);
                
                JSONArray authors = (JSONArray) book.get("authors");
                System.out.println("Authors:");
                for (int j = 0; j < authors.size(); j++) {
                    System.out.println(authors.get(j));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    
    public static void addBookToJson(String title, int publishedYear, int numberOfPages, List<String> authors) {
        JSONParser parser = new JSONParser();
        
        try {
            Object obj = parser.parse(new FileReader("bookshelf.json"));
            JSONObject jsonObject = (JSONObject) obj;
            
            JSONArray books = (JSONArray) jsonObject.get("BookShelf");
            
            // Create new book
            JSONObject newBook = new JSONObject();
            newBook.put("title", title);
            newBook.put("publishedYear", publishedYear);
            newBook.put("numberOfPages", numberOfPages);
            
            JSONArray authorsArray = new JSONArray();
            for (String author : authors) {
                authorsArray.add(author);
            }
            newBook.put("authors", authorsArray);
            
            // Add new book to array
            books.add(newBook);
            
            // Write back to file
            FileWriter file = new FileWriter("bookshelf.json");
            file.write(jsonObject.toJSONString());
            file.flush();
            file.close();
            
            System.out.println("\nBook added to JSON successfully.");
            
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        // Create sample XML and JSON files
        createSampleXmlFile();
        createSampleJsonFile();
        
        // Parse XML and JSON
        parseXmlFile();
        parseJsonFile();
        
        // Add new book programmatically to both formats
        List<String> newAuthors = new ArrayList<>();
        newAuthors.add("Martin Fowler");
        newAuthors.add("Kent Beck");
        
        addBookToXml("Refactoring", 2018, 430, newAuthors);
        addBookToJson("Refactoring", 2018, 430, newAuthors);
        
        // Parse again to confirm additions
        System.out.println("\n------- AFTER ADDING NEW BOOK -------");
        parseXmlFile();
        parseJsonFile();
    }
}