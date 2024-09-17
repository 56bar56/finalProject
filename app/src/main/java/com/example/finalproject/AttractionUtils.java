package com.example.finalproject;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class AttractionUtils {

    private HashMap<String, List<String>> attractionUrls;
    private Random random;

    public AttractionUtils() {
        random = new Random();
        attractionUrls = new HashMap<>();

        // Define the URLs for each type of attraction
        attractionUrls.put("historical", new ArrayList<String>() {{
            add("https://www.usnews.com/object/image/0000016a-e02a-df3a-a36b-fffa11610000/14-mount-rushmore-getty.jpg?update-time=1572431129200&size=responsive640"); //good
            add("https://d2rdhxfof4qmbb.cloudfront.net/wp-content/uploads/20180512193736/Peru1.jpg"); //good
            add("https://www.travelwanderlust.co/wp-content/uploads/2023/09/The-10-best-historical-cities-in-Europe.jpg"); //good
        }});
        attractionUrls.put("museum", new ArrayList<String>() {{
            add("https://cdn.britannica.com/51/194651-050-747F0C18/Interior-National-Gallery-of-Art-Washington-DC.jpg"); //good
            add("https://img.guidebook-sweden.com/stockholms-kommun/gustav-iiis-antikmuseum.jpg"); // good
            add("https://cdn-ijnhp.nitrocdn.com/pywIAllcUPgoWDXtkiXtBgvTOSromKIg/assets/images/optimized/rev-5794eaa/www.jaypeehotels.com/blog/wp-content/uploads/2023/03/blog-5.jpg"); //good
        }});
        attractionUrls.put("park", new ArrayList<String>() {{
            add("https://gttp.imgix.net/418659/x/0/16-must-visit-amusement-parks-theme-parks-and-waterparks-in-the-philippines-13.jpg?auto=compress%2Cformat&ch=Width%2CDPR&dpr=1&ixlib=php-3.3.0&w=883"); //good
            add("https://miro.medium.com/v2/resize:fit:750/0*rWGOVqIgWMofvudp.jpg"); // good
            add("https://assets.simpleviewinc.com/simpleview/image/upload/crm/sanantoniotx/Six-Flags-Fiesta-Texas-Banner_CCB99C8A-5056-BFCE-A81EF5BBB0A6AB83-ccb99a785056bfc_ccb99f7e-5056-bfce-a859b44366f09900.jpg"); //good
        }});
        attractionUrls.put("zoo", new ArrayList<String>() {{
            add("https://aqualifeprojects.com/wp-content/uploads/2018/02/Wroclaw-zoo.jpg"); // good
            add("https://www.sfzoo.org/wp-content/uploads/2021/06/dsc7364-2-10-12-1.jpg"); // good
            add("https://assets.simpleviewinc.com/simpleview/image/upload/c_limit,q_75,w_1200/v1/crm/houston/Asian-Elephant-0479-0654-783906ecce425e6_783908b6-f2dd-dbab-baaafeb9c82c3d70.jpg"); // good
        }});
        attractionUrls.put("nature", new ArrayList<String>() {{
            add("https://www.nationalparks.org/uploads/_1200x630_crop_center-center_82_none/yosemitenp_kyle_yates_istock.jpg?mtime=1655840352"); // good
            add("https://hips.hearstapps.com/hmg-prod/images/sunset-in-yosemite-valley-royalty-free-image-1588783083.jpg"); //good
            add("https://media.cnn.com/api/v1/images/stellar/prod/221213123635-alternative-national-parks-grand-staircase.jpg?c=original"); // good
        }});
        attractionUrls.put("theater", new ArrayList<String>() {{
            add("https://d26oc3sg82pgk3.cloudfront.net/files/media/edit/image/53890/article_full%401x.jpg"); // good
            add("https://en.reformsports.com/oxegrebi/2023/07/Differences-Between-Stadium-and-Arena.jpg"); // good
            add("https://images.seattletimes.com/wp-content/uploads/2021/10/102321_coldplaycpa_tzr.jpg?d=1200x630"); // good
        }});
        attractionUrls.put("beach", new ArrayList<String>() {{
            add("https://www.thetimes.com/imageserver/image/%2Fmethode%2Ftimes%2Fprod%2Fweb%2Fbin%2F46844835-f865-4f0b-87f5-9dc764fdff19.jpg?crop=1564%2C880%2C318%2C0"); // good
            add("https://drupal-prod.visitcalifornia.com/sites/default/files/styles/fluid_1920/public/2020-06/VC_Experiences_ReopeningBeaches_RF_1156532604_1280x640.jpg.webp?itok=uyvrhONk"); // good
            add("https://iresizer.devops.arabiaweather.com/resize?url=https://adminassets.devops.arabiaweather.com/sites/default/files/anse_source_dargent.jpg&size=850x530&force_webp=1"); // good
        }});
        attractionUrls.put("shopping", new ArrayList<String>() {{
            add("https://media.cntravellerme.com/photos/64adaa3b8065f76731bd4b67/16:9/w_2560%2Cc_limit/Hero-Dubai-Mall-Fashion-Avenue-Dubai-Alamy-R9FRB7.jpg"); // good
            add("https://barcelonahacks.com/wp-content/uploads/2023/03/La-Boqueria-1041x567.jpeg"); // good
            add("https://thetourguy.com/wp-content/uploads/2021/12/best-markets-in-london-feature.jpg"); // good
        }});
    }

    // Function to get a random URL based on the type of attraction
    public String getRandomUrlByType(String type) {
        List<String> urls = attractionUrls.get(type);

        // Check if the type exists
        if (urls != null && !urls.isEmpty()) {
            // Return a random URL from the list
            return urls.get(random.nextInt(urls.size()));
        } else {
            // If no URLs for the type, return a default placeholder
            return "https://tourscanner.com/blog/wp-content/uploads/2023/06/best-tourist-attractions-in-Athens.jpg";
        }
    }
}
