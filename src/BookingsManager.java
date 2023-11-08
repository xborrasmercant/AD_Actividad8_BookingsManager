
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;


public class BookingsManager {
    File bookingsFile;
    NodeList bookings;

    public static void main(String[] args) {
        BookingsManager bm = new BookingsManager();

        bm.loadBookingsFile();
        //bm.showBookingByID(2);
    }

    public void addNewBooking(){
        Scanner input = new Scanner(System.in);

        System.out.print("Please, enter your name: ");

    }

    public void showBookingByID(int bID) {
        Boolean bookingFound = false;
        int i = 0;
        Node node;
        Element element;

        try {
            while (!bookingFound) {
                node = bookings.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) node;
                    if (element.getAttribute("location_number").equals(String.valueOf(bID))) {
                        printBookingInfo(element);
                        bookingFound = true;
                    }
                }
                i++;
            }
        }
        catch (NullPointerException e){
            System.out.println("ERROR - Booking with ID: '" + bID + "' has not been found. Please, enter a valid number.");
        }
    }

    public void printBookingInfo(Element element) {
        System.out.println();
        System.out.println("============= Booking " + element.getAttribute("location_number") + " ==============");
        System.out.println("Booking ID: " + element.getAttribute("location_number"));
        System.out.println("Name: " + element.getElementsByTagName("client").item(0).getTextContent());
        System.out.println("Agency: " + element.getElementsByTagName("agency").item(0).getTextContent());
        System.out.println("Price: " + element.getElementsByTagName("price").item(0).getTextContent());
        System.out.println("Room: " + element.getElementsByTagName("room").item(0).getTextContent());
        System.out.println("Hotel: " + element.getElementsByTagName("hotel").item(0).getTextContent());
        System.out.println("Check-in: " + element.getElementsByTagName("check_in").item(0).getTextContent());
        System.out.println("Nights: " + element.getElementsByTagName("room_nights").item(0).getTextContent());
    }

    public void loadBookingsFile() {
        try {
            bookingsFile = new File("src/bookings.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(bookingsFile);

            bookings = doc.getElementsByTagName("booking");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getBookingsFile() {
        return bookingsFile;
    }
    public void setBookingsFile(File bookingsFile) {
        this.bookingsFile = bookingsFile;
    }
    public NodeList getBookings() {
        return bookings;
    }
    public void setBookings(NodeList bookings) {
        this.bookings = bookings;
    }
}