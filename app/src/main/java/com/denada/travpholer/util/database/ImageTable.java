package com.denada.travpholer.util.database;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class ImageTable {

  // Database table
  
  public static final String TABLE_IMAGE = "countries";
  
  public static final String IMAGE_ID           = "countryID";
  public static final String IMAGE_CNAME = "countryName";							//		token
  public static final String IMAGE_LNAME = "localName";						//      date
  public static final String IMAGE_WEBCODE = "webCode";                        //      photo url
  public static final String IMAGE_REGION = "region";                      //
  public static final String IMAGE_CONTINENT      = "continent";
  public static final String IMAGE_LAT      = "latitude";
  public static final String IMAGE_LON      = "longitude";
  public static final String IMAGE_SURFACE      = "surfaceArea";
  public static final String IMAGE_POP      = "population";


  public static void onCreate(SQLiteDatabase database) {
          String sqlBehaviour = "create table if not exists "
                  + TABLE_IMAGE
                  + "("
                  + IMAGE_ID 			+ " countryID text not null, "
                  + IMAGE_CNAME 		+ " text not null, "
                  + IMAGE_LNAME 		+ " text not null,"
                  + IMAGE_WEBCODE 		+ " text not null,"
                  + IMAGE_REGION 	    + " text not null,"
                  + IMAGE_CONTINENT 	 + " text not null,"
                  + IMAGE_LAT 	    + " REAL not null,"
                  + IMAGE_LON 	    + " REAL not null,"
                  + IMAGE_SURFACE 	    + " REAL not null,"
                  + IMAGE_POP 	    + " INTEGER not null"
                  + ");";

          database.execSQL(sqlBehaviour);
//          String sqlBehaviour = "('ABW','Aruba','Aruba','AW','Caribbean','North America',12.5,-69.97,193.0,103000),",
          database.execSQL(USAGETABLE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(ImageTable.class.getName(), "Upgrading database from version "
            + oldVersion + " to " + newVersion
            + ", which will destroy all old data");

    database.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
    onCreate(database);
  }

  public static final String USAGETABLE_CREATE =
          "INSERT INTO `countries` VALUES ('ABW','Aruba','Aruba','AW','Caribbean','North America',12.5,-69.97,193.0,103000)," +
          "('AFG','Afghanistan','Afganistan/Afqanestan','AF','Southern and Central Asia','Asia',34.52,69.18,652090.0,22720000)," +
          "('AGO','Angola','Angola','AO','Central Africa','Africa',-12.5,18.5,1246700.0,12878000)," +
          "('AIA','Anguilla','Anguilla','AI','Caribbean','North America',18.25,-63.17,96.0,8000)," +
          "('ALA','Aland Islands','Åland Islands','AX','Nordic Countries','Europe',60.12,19.9,1580.0,28355)," +
          "('ALB','Albania','Shqipëria','AL','Southern Europe','Europe',40.94,19.71,28748.0,3401200)," +
          "('AND','Andorra','Andorra','AD','Southern Europe','Europe',42.5,1.5,468.0,78000)," +
          "('ANT','Netherlands Antilles','Nederlandse Antillen','AN','Caribbean','North America',12.1,-68.92,800.0,217000)," +
          "('ARE','United Arab Emirates','Al-Imarat al-´Arabiya al-Muttahida','AE','Middle East','Asia',24.0,54.0,83600.0,2441000)," +
          "('ARG','Argentina','Argentina','AR','South America','South America',-34.0,-64.0,2780400.0,37032000)," +
          "('ARM','Armenia','Hajastan','AM','Middle East','Asia',40.0,45.0,29800.0,3520000)," +
          "('ASM','American Samoa','Amerika Samoa','AS','Polynesia','Oceania',-14.33,-170.0,199.0,68000)," +
          "('ATA','Antarctica','','AQ','Antarctica','Antarctica',-83.36,16.52,14000000.0,5000)," +
          "('ATF','French Southern territories','Terres australes françaises','TF','Antarctica','Antarctica',-43.0,67.0,7780.0,0)," +
          "('ATG','Antigua and Barbuda','Antigua and Barbuda','AG','Caribbean','North America',17.05,-61.8,442.0,68000)," +
          "('AUS','Australia','Australia','AU','Australia and New Zealand','Oceania',-33.85,151.13,7741220.0,18886000)," +
          "('AUT','Austria','Österreich','AT','Western Europe','Europe',47.33,13.33,83859.0,8091800)," +
          "('AZE','Azerbaijan','Azärbaycan','AZ','Middle East','Asia',40.5,47.5,86600.0,7734000)," +
          "('BDI','Burundi','Burundi/Uburundi','BI','Eastern Africa','Africa',-3.5,30.0,27834.0,6695000)," +
          "('BEL','Belgium','België/Belgique','BE','Western Europe','Europe',50.62,5.68,30518.0,10239000)," +
          "('BEN','Benin','Bénin','BJ','Western Africa','Africa',9.5,2.25,112622.0,6097000)," +
          "('BES','Caribbean Netherlands','Bonaire, Sint Eustatius, and Saba','BQ','Caribbean','North America',12.16,-68.23,328.0,21133)," +
          "('BFA','Burkina Faso','Burkina Faso','BF','Western Africa','Africa',13.0,-2.0,274000.0,11937000)," +
          "('BGD','Bangladesh','Bangladesh','BD','Southern and Central Asia','Asia',24.0,90.0,143998.0,129155000)," +
          "('BGR','Bulgaria','Balgarija','BG','Eastern Europe','Europe',43.0,25.0,110994.0,8190900)," +
          "('BHR','Bahrain','Al-Bahrayn','BH','Middle East','Asia',26.0,50.55,694.0,617000)," +
          "('BHS','Bahamas','The Bahamas','BS','Caribbean','North America',25.08,-77.35,13878.0,307000)," +
          "('BIH','Bosnia and Herzegovina','Босна и Херцеговина','BA','Southern Europe','Europe',44.2,17.91,51197.0,3972000)," +
          "('BLM','Saint Barthelemy','Saint Barthélemy','BL','Caribbean','North America',17.9,-62.8333,22.0,8900)," +
          "('BLR','Belarus','Беларусь','BY','Eastern Europe','Europe',53.9,27.57,207600.0,10236000)," +
          "('BLZ','Belize','Belize','BZ','Central America','North America',17.25,-88.75,22696.0,241000)," +
          "('BMU','Bermuda','Bermuda','BM','North America','North America',32.33,-64.75,53.0,65000)," +
          "('BOL','Bolivia','Bolivia','BO','South America','South America',-17.0,-65.0,1098581.0,8329000)," +
          "('BRA','Brazil','Brasil','BR','South America','South America',-10.0,-55.0,8547403.0,170115000)," +
          "('BRB','Barbados','Barbados','BB','Caribbean','North America',13.17,-59.53,430.0,270000)," +
          "('BRN','Brunei','Brunei Darussalam','BN','Southeast Asia','Asia',4.5,114.67,5765.0,328000)," +
          "('BTN','Bhutan','Druk-Yul','BT','Southern and Central Asia','Asia',27.5,90.5,47000.0,2124000)," +
          "('BVT','Bouvet Island','Bouvetøya','BV','Antarctica','Antarctica',-54.43,3.4,59.0,0)," +
          "('BWA','Botswana','Botswana','BW','Southern Africa','Africa',-22.0,24.0,581730.0,1622000)," +
          "('CAF','Central African Republic','Centrafrique/Bê-Afrîka','CF','Central Africa','Africa',7.0,21.0,622984.0,3615000)," +
          "('CAN','Canada','Canada','CA','North America','North America',45.45,-73.48,9970610.0,31147000)," +
          "('CBR','Cyberbunker','Republic Cyberbunker','CB','Western Europe','Europe',51.5,3.91,2.0,6)," +
          "('CCK','Cocos (Keeling) Islands','Cocos (Keeling) Islands','CC','Australia and New Zealand','Oceania',-12.5,96.83,14.0,600)," +
          "('CHE','Switzerland','Schweiz/Suisse/Svizzera/Svizra','CH','Western Europe','Europe',47.0,8.0,41284.0,7160400)," +
          "('CHL','Chile','Chile','CL','South America','South America',-30.0,-71.0,756626.0,15211000)," +
          "('CHN','China','中國,中国','CN','Eastern Asia','Asia',35.0,105.0,9572900.0,1277558000)," +
          "('CIV','Côte d’Ivoire','Côte d’Ivoire','CI','Western Africa','Africa',8.0,-5.0,322463.0,14786000)," +
          "('CMR','Cameroon','Cameroun/Cameroon','CM','Central Africa','Africa',6.0,12.0,475442.0,15085000)," +
          "('COD','Congo, Democratic Republic','République Démocratique du Congo','CD','Central Africa','Africa',-1.0,15.0,2344858.0,51654000)," +
          "('COG','Congo','Congo','CG','Central Africa','Africa',-1.0,15.0,342000.0,2943000)," +
          "('COK','Cook Islands','The Cook Islands','CK','Polynesia','Oceania',-21.23,-159.77,236.0,20000)," +
          "('COL','Colombia','Colombia','CO','South America','South America',4.0,-72.0,1138914.0,42321000)," +
          "('COM','Comoros','Komori/Comores','KM','Eastern Africa','Africa',-12.17,44.25,1862.0,578000)," +
          "('CPV','Cape Verde','Cabo Verde','CV','Western Africa','Africa',16.0,-24.0,4033.0,428000)," +
          "('CRI','Costa Rica','Costa Rica','CR','Central America','North America',10.0,-84.0,51100.0,4023000)," +
          "('CUB','Cuba','Cuba','CU','Caribbean','North America',21.5,-80.0,110861.0,11201000)," +
          "('CUW','Curacao','Curaçao','CW','Caribbean','North America',12.25,-68.75,444.0,142000)," +
          "('CXR','Christmas Island','Christmas Island','CX','Australia and New Zealand','Oceania',-10.5,105.67,135.0,2500)," +
          "('CYM','Cayman Islands','Cayman Islands','KY','Caribbean','North America',19.5,-80.5,264.0,38000)," +
          "('CYP','Cyprus','Kýpros/Kibris','CY','Middle East','Asia',35.0,33.0,9251.0,754700)," +
          "('CZE','Czech Republic','¸esko','CZ','Eastern Europe','Europe',49.75,15.5,78866.0,10278100)," +
          "('DEU','Germany','Deutschland','DE','Western Europe','Europe',48.7,9.02,357022.0,82164700)," +
          "('DJI','Djibouti','Djibouti/Jibuti','DJ','Eastern Africa','Africa',11.5,43.0,23200.0,638000)," +
          "('DMA','Dominica','Dominica','DM','Caribbean','North America',15.42,-61.33,751.0,71000)," +
          "('DNK','Denmark','Danmark','DK','Nordic Countries','Europe',55.42,12.17,43094.0,5330000)," +
          "('DOM','Dominican Republic','República Dominicana','DO','Caribbean','North America',19.0,-70.67,48511.0,8495000)," +
          "('DZA','Algeria','Al-Jaza’ir/Algérie','DZ','Northern Africa','Africa',28.0,3.0,2381741.0,31471000)," +
          "('ECU','Ecuador','Ecuador','EC','South America','South America',-2.0,-77.5,283561.0,12646000)," +
          "('EGY','Egypt','Misr','EG','Northern Africa','Africa',27.0,30.0,1001449.0,68470000)," +
          "('ERI','Eritrea','Ertra','ER','Eastern Africa','Africa',15.0,39.0,117600.0,3850000)," +
          "('ESH','Western Sahara','As-Sahrawiya','EH','Northern Africa','Africa',24.5,-13.0,266000.0,293000)," +
          "('ESP','Spain','España','ES','Southern Europe','Europe',40.4,-3.68,505992.0,39441700)," +
          "('EST','Estonia','Eesti','EE','Baltic Countries','Europe',59.0,26.0,45227.0,1439200)," +
          "('ETH','Ethiopia','YeItyop´iya','ET','Eastern Africa','Africa',8.0,38.0,1104300.0,62565000)," +
          "('EUR','European Union','','EU','Western Europe','Europe',47.0,9.14,0.0,501260000)," +
          "('FIN','Finland','Suomi','FI','Nordic Countries','Europe',64.0,26.0,338145.0,5171300)," +
          "('FJI','Fiji Islands','Fiji Islands','FJ','Melanesia','Oceania',-18.0,175.0,18274.0,817000)," +
          "('FLK','Falkland Islands','Falkland Islands','FK','South America','South America',-51.75,-59.0,12173.0,2000)," +
          "('FRA','France','法國,法国,','FR','Western Europe','Europe',47.0,2.0,551500.0,59225700)," +
          "('FRO','Faroe Islands','Føroyar','FO','Nordic Countries','Europe',62.0,-7.0,1399.0,43000)," +
          "('FSM','Micronesia, Federated States','','FM','Micronesia','Oceania',6.92,158.25,702.0,119000)," +
          "('GAB','Gabon','Le Gabon','GA','Central Africa','Africa',-1.0,11.75,267668.0,1226000)," +
          "('GBR','United Kingdom','United Kingdom','GB','British Islands','Europe',52.48,-2.12,242900.0,59623400)," +
          "('GEO','Georgia','Sakartvelo','GE','Middle East','Asia',42.0,43.5,69700.0,4968000)," +
          "('GGY','Guernsey','Bailiwick of Guernsey','GG','British Islands','Europe',49.43,-2.58,63.0,66000)," +
          "('GHA','Ghana','Ghana','GH','Western Africa','Africa',8.0,-2.0,238533.0,20212000)," +
          "('GIB','Gibraltar','Gibraltar','GI','Southern Europe','Europe',36.18,-5.37,6.0,25000)," +
          "('GIN','Guinea','Guinée','GN','Western Africa','Africa',11.0,-10.0,245857.0,7430000)," +
          "('GLP','Guadeloupe','Guadeloupe','GP','Caribbean','North America',16.25,-61.58,1705.0,456000)," +
          "('GMB','Gambia','The Gambia','GM','Western Africa','Africa',13.47,-16.57,11295.0,1305000)," +
          "('GNB','Guinea-Bissau','Guiné-Bissau','GW','Western Africa','Africa',12.0,-15.0,36125.0,1213000)," +
          "('GNQ','Equatorial Guinea','Guinea Ecuatorial','GQ','Central Africa','Africa',2.0,10.0,28051.0,453000)," +
          "('GRC','Greece','Elláda','GR','Southern Europe','Europe',40.48,22.31,131626.0,10545700)," +
          "('GRD','Grenada','Grenada','GD','Caribbean','North America',12.12,-61.67,344.0,94000)," +
          "('GRL','Greenland','Kalaallit Nunaat/Grønland','GL','North America','North America',72.0,-40.0,2166090.0,56000)," +
          "('GTM','Guatemala','Guatemala','GT','Central America','North America',15.5,-90.25,108889.0,11385000)," +
          "('GUF','French Guiana','Guyane française','GF','South America','South America',4.0,-53.0,90000.0,181000)," +
          "('GUM','Guam','Guam','GU','Micronesia','Oceania',13.47,144.78,549.0,168000)," +
          "('GUY','Guyana','Guyana','GY','South America','South America',5.0,-59.0,214969.0,861000)," +
          "('HKG','Hong Kong','香港,香港','HK','Eastern Asia','Asia',22.28,114.15,1075.0,6782000)," +
          "('HMD','Heard Island and McDonald Islands','Heard and McDonald Islands','HM','Australia and New Zealand','Asia',-53.1,72.52,359.0,0)," +
          "('HND','Honduras','Honduras','HN','Central America','North America',15.0,-86.5,112088.0,6485000)," +
          "('HRV','Croatia','Hrvatska','HR','Southern Europe','Europe',45.17,15.5,56538.0,4473000)," +
          "('HTI','Haiti','Haïti/Dayti','HT','Caribbean','North America',19.0,-72.42,27750.0,8222000)," +
          "('HUN','Hungary','Magyarország','HU','Eastern Europe','Europe',47.0,20.0,93030.0,10043200)," +
          "('IDN','Indonesia','Indonesia','ID','Southeast Asia','Asia',-6.9,107.62,1904569.0,212107000)," +
          "('IMN','Isle of Man','','IM','British Islands','Europe',54.23,-4.57,572.0,85000)," +
          "('IND','India','Bharat/India','IN','Southern and Central Asia','Asia',28.47,77.03,3287263.0,1013662000)," +
          "('IOT','British Indian Ocean Territory','British Indian Ocean Territory','IO','Eastern Africa','Africa',-6.0,71.5,78.0,0)," +
          "('IRL','Ireland','Ireland/Éire','IE','British Islands','Europe',53.0,-8.0,70273.0,3775100)," +
          "('IRN','Iran','Iran','IR','Southern and Central Asia','Asia',32.7,51.15,1648195.0,67702000)," +
          "('IRQ','Iraq','Al-´Iraq','IQ','Middle East','Asia',33.0,44.0,438317.0,23115000)," +
          "('ISL','Iceland','Ísland','IS','Nordic Countries','Europe',65.0,-18.0,103000.0,279000)," +
          "('ISR','Israel','Yisra’el/Isra’il','IL','Middle East','Asia',32.08,34.81,21056.0,6217000)," +
          "('ITA','Italy','Italia','IT','Southern Europe','Europe',45.02,8.63,301316.0,57680000)," +
          "('JAM','Jamaica','Jamaica','JM','Caribbean','North America',18.25,-77.5,10990.0,2583000)," +
          "('JEY','Jersey','Bailiwick of Jersey','JE','British Islands','Europe',49.18,-2.1,112.0,98000)," +
          "('JOR','Jordan','Al-Urdunn','JO','Middle East','Asia',31.95,35.93,88946.0,5083000)," +
          "('JPN','Japan','日本,日本','JP','Eastern Asia','Asia',36.0,138.0,377829.0,126714000)," +
          "('KAZ','Kazakstan','Қазақстан Республикасы','KZ','Southern and Central Asia','Asia',43.25,76.95,2724900.0,16223000)," +
          "('KEN','Kenya','Kenya','KE','Eastern Africa','Africa',1.0,38.0,580367.0,30080000)," +
          "('KGZ','Kyrgyzstan','Kyrgyzstan','KG','Southern and Central Asia','Asia',41.0,75.0,199900.0,4699000)," +
          "('KHM','Cambodia','Kâmpuchéa','KH','Southeast Asia','Asia',13.0,105.0,181035.0,11168000)," +
          "('KIR','Kiribati','Kiribati','KI','Micronesia','Oceania',1.42,173.0,726.0,83000)," +
          "('KNA','Saint Kitts and Nevis','Saint Kitts and Nevis','KN','Caribbean','North America',17.33,-62.75,261.0,38000)," +
          "('KOR','South Korea','韓國,韩国','KR','Eastern Asia','Asia',37.57,127.0,99434.0,46844000)," +
          "('KWT','Kuwait','Al-Kuwayt','KW','Middle East','Asia',29.34,47.66,17818.0,1972000)," +
          "('LAO','Laos','Lao','LA','Southeast Asia','Asia',18.0,105.0,236800.0,5433000)," +
          "('LBN','Lebanon','Lubnan','LB','Middle East','Asia',33.83,35.83,10400.0,3282000)," +
          "('LBR','Liberia','Liberia','LR','Western Africa','Africa',6.5,-9.5,111369.0,3154000)," +
          "('LBY','Libya','Libiya','LY','Northern Africa','Africa',25.0,17.0,1759540.0,5605000)," +
          "('LCA','Saint Lucia','Saint Lucia','LC','Caribbean','North America',13.88,-61.13,622.0,154000)," +
          "('LIE','Liechtenstein','Liechtenstein','LI','Western Europe','Europe',47.17,9.53,160.0,32300)," +
          "('LKA','Sri Lanka','Sri Lanka/Ilankai','LK','Southern and Central Asia','Asia',7.0,81.0,65610.0,18827000)," +
          "('LSO','Lesotho','Lesotho','LS','Southern Africa','Africa',-29.5,28.5,30355.0,2153000)," +
          "('LTU','Lithuania','Lietuva','LT','Baltic Countries','Europe',56.0,24.0,65301.0,3698500)," +
          "('LUX','Luxembourg','Luxembourg/Lëtzebuerg','LU','Western Europe','Europe',49.75,6.17,2586.0,435700)," +
          "('LVA','Latvia','Latvija','LV','Baltic Countries','Europe',57.0,25.0,64589.0,2424200)," +
          "('MAC','Macao','澳门,澳門','MO','Eastern Asia','Asia',22.17,113.55,18.0,473000)," +
          "('MAF','Saint Martin','','MF','Caribbean','North America',18.05,-63.08,87.0,77741)," +
          "('MAR','Morocco','Al-Maghrib','MA','Northern Africa','Africa',33.69,-7.39,446550.0,28351000)," +
          "('MCO','Monaco','Monaco','MC','Western Europe','Europe',43.73,7.4,1.5,34000)," +
          "('MDA','Moldova','Moldova','MD','Eastern Europe','Europe',47.0,29.0,33851.0,4380000)," +
          "('MDG','Madagascar','Madagasikara/Madagascar','MG','Eastern Africa','Africa',-20.0,47.0,587041.0,15942000)," +
          "('MDV','Maldives','Dhivehi Raajje/Maldives','MV','Southern and Central Asia','Asia',3.25,73.0,298.0,286000)," +
          "('MEX','Mexico','México','MX','Central America','North America',23.0,-102.0,1958201.0,98881000)," +
          "('MHL','Marshall Islands','Marshall Islands/Majol','MH','Micronesia','Oceania',9.0,168.0,181.0,64000)," +
          "('MKD','Macedonia','Makedonija','MK','Southern Europe','Europe',41.83,22.0,25713.0,2024000)," +
          "('MLI','Mali','Mali','ML','Western Africa','Africa',17.0,-4.0,1240192.0,11234000)," +
          "('MLT','Malta','Malta','MT','Southern Europe','Europe',35.83,14.58,316.0,380200)," +
          "('MMR','Myanmar','Myanma Pye','MM','Southeast Asia','Asia',22.0,98.0,676578.0,45611000)," +
          "('MNE','Montenegro','Црна Гора','ME','Eastern Europe','Europe',42.0,19.0,13812.0,672180)," +
          "('MNG','Mongolia','蒙古,蒙古','MN','Eastern Asia','Asia',46.0,105.0,1566500.0,2662000)," +
          "('MNP','Northern Mariana Islands','Northern Mariana Islands','MP','Micronesia','Oceania',15.2,145.75,464.0,78000)," +
          "('MOZ','Mozambique','Moçambique','MZ','Eastern Africa','Africa',-18.25,35.0,801590.0,19680000)," +
          "('MRT','Mauritania','Muritaniya/Mauritanie','MR','Western Africa','Africa',20.0,-12.0,1025520.0,2670000)," +
          "('MSR','Montserrat','Montserrat','MS','Caribbean','North America',16.75,-62.2,102.0,11000)," +
          "('MTQ','Martinique','Martinique','MQ','Caribbean','North America',14.7,-61.0,1102.0,395000)," +
          "('MUS','Mauritius','Mauritius','MU','Eastern Africa','Africa',-20.28,57.55,2040.0,1158000)," +
          "('MWI','Malawi','Malawi','MW','Eastern Africa','Africa',-13.5,34.0,118484.0,10925000)," +
          "('MYS','Malaysia','Malaysia','MY','Southeast Asia','Asia',3.17,101.7,329758.0,22244000)," +
          "('MYT','Mayotte','Mayotte','YT','Eastern Africa','Africa',-12.83,45.17,373.0,149000)," +
          "('NAM','Namibia','Namibia','NA','Southern Africa','Africa',-22.0,17.0,824292.0,1726000)," +
          "('NCL','New Caledonia','Nouvelle-Calédonie','NC','Melanesia','Oceania',-21.5,165.5,18575.0,214000)," +
          "('NER','Niger','Niger','NE','Western Africa','Africa',16.0,8.0,1267000.0,10730000)," +
          "('NFK','Norfolk Island','Norfolk Island','NF','Australia and New Zealand','Oceania',-29.03,167.95,36.0,2000)," +
          "('NGA','Nigeria','Nigeria','NG','Western Africa','Africa',10.0,8.0,923768.0,111506000)," +
          "('NIC','Nicaragua','Nicaragua','NI','Central America','North America',13.0,-85.0,130000.0,5074000)," +
          "('NIU','Niue','Niue','NU','Polynesia','Oceania',-19.03,-169.87,260.0,2000)," +
          "('NLD','Netherlands','Nederland','NL','Western Europe','Europe',52.5,5.75,41526.0,15864000)," +
          "('NOR','Norway','Norge','NO','Nordic Countries','Europe',62.0,10.0,323877.0,4478500)," +
          "('NPL','Nepal','Nepal','NP','Southern and Central Asia','Asia',28.0,84.0,147181.0,23930000)," +
          "('NRU','Nauru','Naoero/Nauru','NR','Micronesia','Oceania',-0.53,166.92,21.0,12000)," +
          "('NZL','New Zealand','New Zealand/Aotearoa','NZ','Australia and New Zealand','Oceania',-43.53,172.63,270534.0,3862000)," +
          "('OMN','Oman','´Uman','OM','Middle East','Asia',23.61,58.59,309500.0,2542000)," +
          "('PAK','Pakistan','Pakistan','PK','Southern and Central Asia','Asia',30.0,70.0,796095.0,156483000)," +
          "('PAN','Panama','Panamá','PA','Central America','North America',9.0,-80.0,75517.0,2856000)," +
          "('PCN','Pitcairn','Pitcairn','PN','Polynesia','Oceania',-24.36,-128.32,49.0,50)," +
          "('PER','Peru','Perú/Piruw','PE','South America','South America',-10.0,-76.0,1285216.0,25662000)," +
          "('PHL','Philippines','Pilipinas','PH','Southeast Asia','Asia',13.0,122.0,300000.0,75967000)," +
          "('PLW','Palau','Belau/Palau','PW','Micronesia','Oceania',7.5,134.5,459.0,19000)," +
          "('PNG','Papua New Guinea','Papua New Guinea/Papua Niugini','PG','Melanesia','Oceania',-6.0,147.0,462840.0,4807000)," +
          "('POL','Poland','Polska','PL','Eastern Europe','Europe',52.0,20.0,323250.0,38653600)," +
          "('PRI','Puerto Rico','Puerto Rico','PR','Caribbean','North America',18.25,-66.5,8875.0,3869000)," +
          "('PRK','North Korea','Choson Minjujuui In´min Konghwaguk (Bukhan)','KP','Eastern Asia','Asia',40.0,127.0,120538.0,24039000)," +
          "('PRT','Portugal','Portugal','PT','Southern Europe','Europe',39.5,-8.0,91982.0,9997600)," +
          "('PRY','Paraguay','Paraguay','PY','South America','South America',-23.0,-58.0,406752.0,5496000)," +
          "('PSE','Palestine','Filastin','PS','Middle East','Asia',31.5,34.47,6257.0,3101000)," +
          "('PYF','French Polynesia','Polynésie française','PF','Polynesia','Oceania',-15.0,-140.0,4000.0,235000)," +
          "('QAT','Qatar','Qatar','QA','Middle East','Asia',25.5,51.25,11000.0,599000)," +
          "('REU','Réunion','Réunion','RE','Eastern Africa','Africa',-21.1,55.6,2510.0,699000)," +
          "('ROM','Romania','România','RO','Eastern Europe','Europe',46.0,25.0,238391.0,22455500)," +
          "('RUS','Russia','Российская Федерация','RU','Eastern Europe','Europe',60.0,100.0,17075400.0,146934000)," +
          "('RWA','Rwanda','Rwanda/Urwanda','RW','Eastern Africa','Africa',-2.0,30.0,26338.0,7733000)," +
          "('SAU','Saudi Arabia','Al-´Arabiya as-Sa´udiya','SA','Middle East','Asia',25.0,45.0,2149690.0,21607000)," +
          "('SDN','Sudan','As-Sudan','SD','Northern Africa','Africa',15.0,30.0,2505813.0,29490000)," +
          "('SEN','Senegal','Sénégal/Sounougal','SN','Western Africa','Africa',14.0,-14.0,196722.0,9481000)," +
          "('SGP','Singapore','Singapore/Singapura/Xinjiapo/Singapur','SG','Southeast Asia','Asia',1.37,103.8,618.0,3567000)," +
          "('SGS','South Georgia and the South Sandwich Islands','','GS','Antarctica','Antarctica',-54.5,-36.5,3903.0,0)," +
          "('SHN','Saint Helena','Saint Helena','SH','Western Africa','Africa',-15.93,-5.7,314.0,6000)," +
          "('SJM','Svalbard and Jan Mayen','Svalbard og Jan Mayen','SJ','Nordic Countries','Europe',78.0,18.0,62422.0,3200)," +
          "('SLB','Solomon Islands','Solomon Islands','SB','Melanesia','Oceania',-8.0,159.0,28896.0,444000)," +
          "('SLE','Sierra Leone','Sierra Leone','SL','Western Africa','Africa',8.5,-11.5,71740.0,4854000)," +
          "('SLV','El Salvador','El Salvador','SV','Central America','North America',13.83,-88.92,21041.0,6276000)," +
          "('SMR','San Marino','San Marino','SM','Southern Europe','Europe',43.93,12.47,61.0,27000)," +
          "('SOM','Somalia','Soomaaliya','SO','Eastern Africa','Africa',10.0,49.0,637657.0,10097000)," +
          "('SPM','Saint Pierre and Miquelon','Saint-Pierre-et-Miquelon','PM','North America','North America',46.83,-56.33,242.0,7000)," +
          "('SRB','Serbia','Република Србија','RS','Eastern Europe','Europe',44.0,21.0,88361.0,7334935)," +
          "('SSD','South Sudan','Republic of South Sudan','SS','Northern Africa','Africa',4.8,31.6,619745.0,8260000)," +
          "('STP','Sao Tome and Principe','São Tomé e Príncipe','ST','Central Africa','Africa',0.22,6.44,964.0,147000)," +
          "('SUR','Suriname','Suriname','SR','South America','South America',4.0,-56.0,163265.0,417000)," +
          "('SVK','Slovakia','Slovensko','SK','Eastern Europe','Europe',48.68,17.37,49012.0,5398700)," +
          "('SVN','Slovenia','Slovenija','SI','Southern Europe','Europe',46.0,15.0,20256.0,1987800)," +
          "('SWE','Sweden','Sverige','SE','Nordic Countries','Europe',55.7,13.18,449964.0,8861400)," +
          "('SWZ','Swaziland','kaNgwane','SZ','Southern Africa','Africa',-26.5,31.5,17364.0,1008000)," +
          "('SXM','Sint Maarten','Sint Maarten','SX','Caribbean','North America',18.0667,-63.05,1100.0,37429)," +
          "('SYC','Seychelles','Sesel/Seychelles','SC','Eastern Africa','Africa',-4.58,55.67,455.0,77000)," +
          "('SYR','Syria','Suriya','SY','Middle East','Asia',33.5,36.3,185180.0,16125000)," +
          "('TCA','Turks and Caicos Islands','The Turks and Caicos Islands','TC','Caribbean','North America',21.75,-71.58,430.0,17000)," +
          "('TCD','Chad','Tchad/Tshad','TD','Central Africa','Africa',15.0,19.0,1284000.0,7651000)," +
          "('TGO','Togo','Togo','TG','Western Africa','Africa',8.0,1.17,56785.0,4629000)," +
          "('THA','Thailand','Prathet Thai','TH','Southeast Asia','Asia',13.75,100.52,513115.0,61399000)," +
          "('TJK','Tajikistan','Toçikiston','TJ','Southern and Central Asia','Asia',39.0,71.0,143100.0,6188000)," +
          "('TKL','Tokelau','','TK','Polynesia','Oceania',-9.2,-171.8,12.0,2000)," +
          "('TKM','Turkmenistan','Türkmenostan','TM','Southern and Central Asia','Asia',40.0,60.0,488100.0,4459000)," +
          "('TMP','East Timor','Timor Timur','TP','Southeast Asia','Asia',-8.6,125.34,14874.0,885000)," +
          "('TON','Tonga','Tonga','TO','Polynesia','Oceania',-20.0,-175.0,650.0,99000)," +
          "('TTO','Trinidad and Tobago','Trinidad and Tobago','TT','Caribbean','North America',11.0,-61.0,5130.0,1295000)," +
          "('TUN','Tunisia','Tunis/Tunisie','TN','Northern Africa','Africa',34.0,9.0,163610.0,9586000)," +
          "('TUR','Turkey','Türkiye','TR','Middle East','Asia',39.0,35.0,774815.0,66591000)," +
          "('TUV','Tuvalu','Tuvalu','TV','Polynesia','Oceania',-8.0,178.0,26.0,12000)," +
          "('TWN','Taiwan','台灣,台湾','TW','Eastern Asia','Asia',23.5,121.0,36188.0,22256000)," +
          "('TZA','Tanzania','Tanzania','TZ','Eastern Africa','Africa',-6.0,35.0,883749.0,33517000)," +
          "('UGA','Uganda','Uganda','UG','Eastern Africa','Africa',1.0,32.0,241038.0,21778000)," +
          "('UKR','Ukraine','Україна','UA','Eastern Europe','Europe',50.62,26.25,603700.0,50456000)," +
          "('UMI','United States Minor Outlying Islands','United States Minor Outlying Islands','UM','Micronesia/Caribbean','Oceania',19.28,166.6,16.0,0)," +
          "('URY','Uruguay','Uruguay','UY','South America','South America',-33.0,-56.0,175016.0,3337000)," +
          "('USA','USA','United States','US','North America','North America',38.0,-97.0,9363520.0,278357000)," +
          "('UZB','Uzbekistan','Uzbekiston','UZ','Southern and Central Asia','Asia',41.0,64.0,447400.0,24318000)," +
          "('VAT','Vatican (Holy See)','Santa Sede/Città del Vaticano','VA','Southern Europe','Europe',41.9,12.46,0.0,1000)," +
          "('VCT','Saint Vincent and the Grenadines','Saint Vincent and the Grenadines','VC','Caribbean','North America',13.25,-61.2,388.0,114000)," +
          "('VEN','Venezuela','Venezuela','VE','South America','South America',8.0,-66.0,912050.0,24170000)," +
          "('VGB','Virgin Islands, British','British Virgin Islands','VG','Caribbean','North America',18.5,-64.5,151.0,21000)," +
          "('VIR','Virgin Islands, U.S.','Virgin Islands of the United States','VI','Caribbean','North America',18.33,-64.83,347.0,93000)," +
          "('VNM','Vietnam','Viêt Nam','VN','Southeast Asia','Asia',10.35,106.35,331689.0,79832000)," +
          "('VUT','Vanuatu','Vanuatu','VU','Melanesia','Oceania',-16.0,167.0,12189.0,190000)," +
          "('WLF','Wallis and Futuna','Wallis-et-Futuna','WF','Polynesia','Oceania',-13.3,-176.2,200.0,15000)," +
          "('WSM','Samoa','Samoa','WS','Polynesia','Oceania',-13.58,-172.33,2831.0,180000)," +
          "('XKX','Kosovo','Republika e Kosovës','XK','Eastern Europe','Europe',42.5,20.8,10908.0,1860000)," +
          "('YEM','Yemen','Al-Yaman','YE','Middle East','Asia',15.4,47.77,527968.0,18112000)," +
          "('ZAF','South Africa','South Africa','ZA','Southern Africa','Africa',-29.0,24.0,1221037.0,40377000)," +
          "('ZMB','Zambia','Zambia','ZM','Eastern Africa','Africa',-15.0,30.0,752618.0,9169000)," +
          "('ZZ1','Other','Other','OT','','',-25.67321,79.240108,0.0,0)," +
          "('ZWE','Zimbabwe','Zimbabwe','ZW','Eastern Africa','Africa',-20.0,30.0,390757.0,11669000)" +
          ";";
} 