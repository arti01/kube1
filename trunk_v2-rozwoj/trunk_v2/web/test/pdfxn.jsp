<%@
page import="java.io.*,java.util.*,com.lowagie.text.*,com.lowagie.text.pdf.*,com.esoftking.fmc.hibernate.utils.*,com.esoftking.fmc.hibernate.app.*,com.esoftking.fmc.hibernate.dao.*,java.sql.*"
pageEncoding="UTF-8"
%><%//
			// Template JSP file for iText
			// by Tal Liron
			//
			String fmcId = (String) request.getAttribute("sfmcid");
			String stName = (String) request.getAttribute("stationName");
			String myear = (String) request.getSession().getAttribute(
					"monthYear");
			String fname = "" + fmcId.trim() + "_" + myear.replaceAll("-", "_") + ".pdf";
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fname);
			response.setCharacterEncoding("UTF-8");

			int section = ((Integer)request.getAttribute("section")).intValue();
			// step 1: creation of a document-object
			Document document = new Document(PageSize.A4,15,15,10,10);

			// step 2:
			// we create a writer that listens to the document
			// and directs a PDF-stream to a temporary buffer

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, buffer);

			// step 3: we open the document
			document.open();

			int dblcount=0;
			if(request.getAttribute("dblCount")!=null)
				dblcount=1;
			UserManager um = new UserManager();
			
			
			// step 4: we add a paragraph to the document

			BaseFont f = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
			Font ff = new Font(f, 8, Font.NORMAL);
			Font sff = new Font(f, 7, Font.NORMAL);
			Font ssff = new Font(f, 6, Font.NORMAL);
			Font ffb = new Font(f, 8, Font.BOLD);
			Font ffb10 = new Font(f, 10, Font.BOLD);
			Font fbold = new Font(f, 12, Font.BOLDITALIC);

			//String imgb = ((String)request.getAttribute("logogif"));
			String URL_ROOT = getServletContext().getRealPath(".");
			String URL_IMAGES = URL_ROOT + "/resources/wydruki/logo_druk.png";
			Image gif = Image.getInstance(URL_IMAGES);
			//gif.scalePercent((float)0.5);

			gif.setAlignment(Image.RIGHT);
			
			//document.add(gif);
			Paragraph p0 = new Paragraph(((String)request.getSession().getAttribute("companyName")).trim().toUpperCase()+"\n",ffb10);
			
			Paragraph p = new Paragraph(
					"INDYWIDUALNA KARTA CZASU PRACY PRACOWNIKA", fbold);
			Paragraph p1 = new Paragraph(stName+"\n",ffb10);
			
			p0.add(p);
			p0.add(p1);
			p0.setAlignment("center");
			
			int cdis=0;
			if(request.getAttribute("cdis")!=null)
				cdis=1;
			//System.out.println("CDIS: RAPORT: "+cdis);
			//document.add(p);
	
			PdfPTable titletable = new PdfPTable(2);
			titletable.setWidthPercentage((float)98);
			int[] wids = {90,10};
			titletable.setWidths(wids);
			PdfPCell cp = new PdfPCell(p0);
			cp.setHorizontalAlignment(Element.ALIGN_CENTER);
			cp.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cp.setBorder(0);
			
			PdfPCell cgif = new PdfPCell(gif);
			cgif.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cgif.setBorder(0);
			
			titletable.addCell(cp);
			titletable.addCell(cgif);
			
			document.add(titletable);
			
			//document.setMargins(20,20,20,20);
			PdfPTable practable = new PdfPTable(2);
			practable.setWidthPercentage((float)98);
			Paragraph im = new Paragraph((String) request.getAttribute("imie") + ":  ", ff);
			im.add(new Paragraph((String) request.getAttribute("sfname"),ffb));
			
			Paragraph naz = new Paragraph("Nazwisko:  ", ff);
			naz.add(new Paragraph((String) request.getAttribute("slname"),ffb));
			
			PdfPCell imcell = new PdfPCell(im);
			imcell.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell nazcell = new PdfPCell(naz);
			nazcell.setHorizontalAlignment(Element.ALIGN_LEFT);

			practable.addCell(imcell);
			practable.addCell(nazcell);
			document.add(practable);

			PdfPTable danetable = new PdfPTable(2);
			danetable.setWidthPercentage((float)98);
			Paragraph nrewi = new Paragraph("Nr ewidencyjny:  " , ff);
			nrewi.add(new Paragraph(fmcId,ffb));
			
			Paragraph mies = new Paragraph((String) request
					.getAttribute("miesiac")
					+ ":  " , ff);
			mies.add(new Paragraph((String) request.getAttribute("my"),ffb));
			
			//Paragraph kwartal = new Paragraph(""+(String)request.getAttribute("kwartaldesc")+":  ", ff);
			Paragraph kwartal = new Paragraph("Okres rozliczeniowy:  ", ff);
			kwartal.add(new Paragraph(((String) request.getAttribute("kwartval")).replaceAll("<br>"," "),ffb));
			
			
			Paragraph nkwartal = new Paragraph("Nominalny kwartalny czas pracy:  " , ff);
			nkwartal.add(new Paragraph((String)request.getAttribute("nkwartval"),ffb));
			
			Paragraph statusTime = new Paragraph("Wypracowany vs zaplanowany:  " , ff);
			statusTime.add(new Paragraph((String) request.getAttribute("sTime"),ffb));
			
			Paragraph nstatusTime = new Paragraph("Wypracowany vs nominalny:  " , ff);
			nstatusTime.add(new Paragraph((String) request.getAttribute("ntotalTime"),ffb));
			
			
			PdfPCell nrewicell = new PdfPCell(nrewi);
			nrewicell.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell miescell = new PdfPCell(mies);
			miescell.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell kwarcell = new PdfPCell(kwartal);
			kwarcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			
			PdfPCell kwarsTime = new PdfPCell(statusTime);
			kwarsTime.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell nkwarcell = new PdfPCell(nkwartal);
			nkwarcell.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell nkwarsTime = new PdfPCell(nstatusTime);
			nkwarsTime.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell nl = new PdfPCell(new Paragraph("\n",sff));
			nl.setColspan(2);
			nl.setBorder(0);
			
			danetable.addCell(nrewicell);
			danetable.addCell(miescell);
			danetable.addCell(nl);
			danetable.addCell(kwarcell);
			danetable.addCell(nkwarcell);
			danetable.addCell(kwarsTime);
			danetable.addCell(nkwarsTime);
			
			//------------------------------------------
			PdfContentByte cb = writer.getDirectContent();
			cb.beginText();
			cb.setFontAndSize(f, 6);
			
			cb.setTextMatrix(0, 1, -1, 0, 15, 400);
            cb.showText((String)request.getAttribute("copyright"));
            cb.endText();
			//------------------------------------------

			danetable.addCell(nl);
			document.add(danetable);

			PdfPTable czastable = new PdfPTable(7);
			czastable.setWidthPercentage((float)98);
			czastable.getDefaultCell().setPhrase(new Phrase("UTF-8"));
			czastable.getDefaultCell().setPaddingBottom(0);
			czastable.getDefaultCell().setPaddingLeft(0);
			czastable.getDefaultCell().setPaddingRight(0);
			czastable.getDefaultCell().setPaddingTop(0);
			//PdfPCell c = new PdfPCell();
			//int[] w = { 8, 12, 12, 12, 12, 11, 11, 11, 11 };
			int[] w = { 8, 18, 18, 10 , 18, 18, 10};
			
			java.awt.Color sobota = new java.awt.Color(200, 200, 200);
			java.awt.Color niedziela = new java.awt.Color(150, 150, 150); 
			
			czastable.setWidths(w);
			//			czastable.addCell("Lp");
			Paragraph lp = new Paragraph("Dz.", ff);
			Paragraph grafik = new Paragraph("Grafik", ff);
			Paragraph czaspracy = new Paragraph("Czas pracy", ff);
			Paragraph gotowosc = new Paragraph("Dyżur telefoniczny", ff);
			Paragraph dyzurmed = new Paragraph((String) request
					.getAttribute("medyczny"), ff);

			PdfPCell lpp = new PdfPCell(lp);
			lpp.setBackgroundColor(new java.awt.Color(180, 180, 180));
			lpp.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell grafikp = new PdfPCell(grafik);
			grafikp.setBackgroundColor(new java.awt.Color(180, 180, 180));
			grafikp.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell czasp = new PdfPCell(czaspracy);
			czasp.setBackgroundColor(new java.awt.Color(180, 180, 180));

			czasp.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell gotow = new PdfPCell(gotowosc);
			gotow.setHorizontalAlignment(Element.ALIGN_CENTER);
			gotow.setBackgroundColor(new java.awt.Color(180, 180, 180));

			PdfPCell dyzur = new PdfPCell(dyzurmed);
			dyzur.setHorizontalAlignment(Element.ALIGN_CENTER);
			dyzur.setBackgroundColor(new java.awt.Color(180, 180, 180));

			//lpp.setBorderColor(new java.awt.Color(0,0,0));
			czastable.addCell(lpp);
			
			grafikp.setColspan(3);
			czastable.addCell(grafikp);
						
			czasp.setColspan(3);
			czastable.addCell(czasp);
			
			gotow.setColspan(2);
			//czastable.addCell(gotow);
			
			dyzur.setColspan(2);
			//czastable.addCell(dyzur);

			czastable.addCell("");
			Paragraph odp = new Paragraph("Od", ff);
			PdfPCell odc = new PdfPCell(odp);
			odc.setHorizontalAlignment(Element.ALIGN_CENTER);
			Paragraph dop = new Paragraph("Do", ff);
			PdfPCell doc = new PdfPCell(dop);
			doc.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph czp = new Paragraph("Czas", ff);
			PdfPCell czc = new PdfPCell(czp);
			czc.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			
			czastable.addCell(odc);
			czastable.addCell(doc);
			czastable.addCell(czc);
			czastable.addCell(odc);
			czastable.addCell(doc);
			czastable.addCell(czc);
			//czastable.addCell(odc);
			//czastable.addCell(doc);
			//czastable.addCell(odc);
			//czastable.addCell(doc);

			ArrayList al = (ArrayList) request.getAttribute("alist");
			String sch = (String) request.getAttribute("sch");
			String userN = (String)request.getAttribute("userNominal");
			ArrayList<String> userNT = (ArrayList)request.getAttribute("userNominalTab");
			
			Phrase pr = null, plp = null;
			PdfPCell cpr = null, cplp = null;
			for (int i = 0; i < al.size(); i++) {
				String slp = String.valueOf(i + 1);
				if(sch.charAt(i)=='n')
					slp += "(N)";

				plp = new Phrase(slp, sff);
				cplp = new PdfPCell(plp);
				if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
					cplp.setBackgroundColor(niedziela);
				else
				if(sch.charAt(i)=='s')
					cplp.setBackgroundColor(sobota);
				cplp.setHorizontalAlignment(Element.ALIGN_CENTER);
			
				userN = userNT.get(i);
				czastable.addCell(cplp);
//---------------------------------------------------------------------------
				String spod="";
				if(section==0)
					spod = ((EcpElement) al.get(i)).getPod();
				if(section==2)
					spod = ((EcpElement) al.get(i)).getOpod();
				String spdo = "";
				Phrase pod = null;
				int ilekol=1;
				int nieobpl = 0;
				if(section==0)
					nieobpl = ((EcpElement) al.get(i)).getIdnieobecplan();
				if(section==2)
					nieobpl = ((EcpElement) al.get(i)).getOidnieobecplan();
				
				if (nieobpl != 0) {
					if(section==0)
						spod = ((EcpElement) al.get(i)).getSnieobecplan();
					if(section==2)
						spod = ((EcpElement) al.get(i)).getSonieobecplan();
					pod = new Phrase(spod, sff);
				} else
					pod = new Phrase(spod, sff);

				PdfPCell cpod = new PdfPCell(pod);
				if (nieobpl>3 && nieobpl<50 && nieobpl!=32 && nieobpl!=33)
					ilekol=2;
				else if(nieobpl>49 || nieobpl==32 || nieobpl==33)
					ilekol=3;
				else
					ilekol=1;
				
				cpod.setColspan(ilekol);
				cpod.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
					cpod.setBackgroundColor(niedziela);
				else
				if(sch.charAt(i)=='s')
					cpod.setBackgroundColor(sobota);
				czastable.addCell(cpod);

				
				if (spod.length() == 5) {
					if(section==0)
						spdo = ((EcpElement) al.get(i)).getPdo();
					if(section==2)
						spdo = ((EcpElement) al.get(i)).getOpdo();
					
					Phrase pdo = new Phrase(spdo,sff);
					PdfPCell cpdo = new PdfPCell(pdo);
					cpdo.setHorizontalAlignment(Element.ALIGN_CENTER);
					if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
						cpdo.setBackgroundColor(niedziela);
					else
					if(sch.charAt(i)=='s')
						cpdo.setBackgroundColor(sobota);
					czastable.addCell(cpdo);
				}else if(nieobpl==0 && spod.compareTo("")==0){
					Phrase pdo = new Phrase(" ",sff);
					PdfPCell cpdo = new PdfPCell(pdo);
					cpdo.setHorizontalAlignment(Element.ALIGN_CENTER);
					if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
						cpdo.setBackgroundColor(niedziela);
					else
					if(sch.charAt(i)=='s')
						cpdo.setBackgroundColor(sobota);
					czastable.addCell(cpdo);
				}
				String stout = "";
				if((nieobpl==0 && spod.compareTo("")!=0) || nieobpl!=0){
				if(nieobpl>3 && nieobpl<50 && nieobpl!=32 && nieobpl!=33){
					//Phrase ppcz1 = new Phrase(""+stout.substring(0,stout.indexOf(":"))+"h "+stout.substring(stout.indexOf(":")+1,stout.length())+"m",sff);
					if((sch.charAt(i)=='n' || sch.charAt(i)=='s' || sch.charAt(i)=='w'))
						stout="00:00";
					else
						if(nieobpl==10)
							stout="08:00";
						else
							stout=userN;
					//System.out.println("###########1["+i+"] uNominal: >"+stout+"<    >"+stout.substring(0,stout.indexOf(":"))+"<    >"+stout.substring(stout.indexOf(":")+1,stout.length()).trim()+"<");
					String hstr = new String(stout.substring(0,stout.indexOf(":")));
					String mstr = stout.substring(stout.indexOf(":")+1,stout.length()).trim();
					String sstr = ""+Integer.valueOf(hstr)+"h "+Integer.valueOf(mstr)+"m";
					if(cdis==1){
						if(section==0)	
							sstr = sstr+" ("+((EcpElement) al.get(i)).getPcids()+")";
						if(section==2)
							sstr = sstr+" ("+((EcpElement) al.get(i)).getOpcids()+")";
					}
					Phrase ppcz1 = new Phrase(sstr,sff);
					
					//Phrase ppcz1 = new Phrase((String)request.getAttribute("userNominal"),sff);
					PdfPCell czcc1 = new PdfPCell(ppcz1);
					czcc1.setHorizontalAlignment(Element.ALIGN_CENTER);
					if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
						czcc1.setBackgroundColor(niedziela);
					else
					if(sch.charAt(i)=='s')
						czcc1.setBackgroundColor(sobota);
					czastable.addCell(czcc1);
				}else if(nieobpl==0){
					if(spod.compareTo(spdo)==0)
						stout="24:00";
					else{
					Time t1 = Time.valueOf(spod+":00");
					Time t2 = Time.valueOf(spdo+":00");
					long ltout = t2.getTime() - t1.getTime() - 3600000;
					Time tout = new Time(ltout);
					stout = tout.toString().substring(0,5);
					}
					//System.out.println("STOUT2["+i+"]: "+stout);
					String hstr = new String(stout.substring(0,stout.indexOf(":")));
					String mstr = stout.substring(stout.indexOf(":")+1,stout.length()).trim();
					String sstr = ""+Integer.valueOf(hstr)+"h "+Integer.valueOf(mstr)+"m";
					if(cdis==1)
					{
						if(section==0)	
							sstr = sstr+" ("+((EcpElement) al.get(i)).getPcids()+")";
						if(section==2)
							sstr = sstr+" ("+((EcpElement) al.get(i)).getOpcids()+")";
					}
						//sstr = sstr+" ("+((EcpElement) al.get(i)).getPcids()+")";
					Phrase ppcz1 = new Phrase(sstr,sff);
					//Phrase ppcz1 = new Phrase(""+Integer.valueOf(hstr)+"h "+Integer.valueOf(mstr)+"m",sff);
					PdfPCell czcc1 = new PdfPCell(ppcz1);
					czcc1.setHorizontalAlignment(Element.ALIGN_CENTER);
					if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
						czcc1.setBackgroundColor(niedziela);
					else
					if(sch.charAt(i)=='s')
						czcc1.setBackgroundColor(sobota);
					czastable.addCell(czcc1);

				}
				}else{
					Phrase ppcz1 = new Phrase(" ",sff);
					PdfPCell czcc1 = new PdfPCell(ppcz1);
					czcc1.setHorizontalAlignment(Element.ALIGN_CENTER);
					if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
						czcc1.setBackgroundColor(niedziela);
					else
					if(sch.charAt(i)=='s')
						czcc1.setBackgroundColor(sobota);
					czastable.addCell(czcc1);
					
				}
					
				
//---------------------------------------------------------------------------
				String srod = "";
				if(section==0)
					srod = ((EcpElement) al.get(i)).getRod();
				if(section==2)
					srod = ((EcpElement) al.get(i)).getOrod();
				
				Phrase rod = null;
				ilekol=1;
				int nieobreal = 0;
				if(section==0)
					nieobreal = ((EcpElement) al.get(i)).getIdnieobecreal();
				if(section==2)
					nieobreal = ((EcpElement) al.get(i)).getOidnieobecreal();
				
				if (nieobreal != 0) {
					if(section==0)
						srod = ((EcpElement) al.get(i)).getSnieobecreal();
					if(section==2)
						srod = ((EcpElement) al.get(i)).getSonieobecreal();
					rod = new Phrase(srod, sff);
				} else
					rod = new Phrase(srod, sff);

				PdfPCell crod = new PdfPCell(rod);
				if (nieobreal>3 && nieobreal<50 && nieobreal!=32 && nieobreal!=33)
					ilekol=2;
				else if(nieobreal>49 || nieobreal==32 || nieobreal==33)
					ilekol=3;
				else
					ilekol=1;
				
				
				crod.setColspan(ilekol);
				crod.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
					crod.setBackgroundColor(niedziela);
				else
				if(sch.charAt(i)=='s')
					crod.setBackgroundColor(sobota);
				czastable.addCell(crod);

				String srdo = "";
				if (srod.length() == 5) {
					if(section==0)
						srdo = ((EcpElement) al.get(i)).getRdo();
					if(section==2)
						srdo = ((EcpElement) al.get(i)).getOrdo();
					
					String ho="";
					if(((String)((EcpElement) al.get(i)).getRlist().get(0)).indexOf("|ho")!=-1)
						ho=" (HO)";
					Phrase rdo = new Phrase(srdo+ho,
							sff);
					PdfPCell crdo = new PdfPCell(rdo);
					crdo.setHorizontalAlignment(Element.ALIGN_CENTER);
					if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
						crdo.setBackgroundColor(niedziela);
					else
					if(sch.charAt(i)=='s')
						crdo.setBackgroundColor(sobota);
					czastable.addCell(crdo);
				}else if(nieobreal==0 && srod.compareTo("")==0){
					Phrase rdo = new Phrase(" ",sff);
					PdfPCell crdo = new PdfPCell(rdo);
					crdo.setHorizontalAlignment(Element.ALIGN_CENTER);
					if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
						crdo.setBackgroundColor(niedziela);
					else
					if(sch.charAt(i)=='s')
						crdo.setBackgroundColor(sobota);
					czastable.addCell(crdo);
				}
				
				if((nieobreal==0 && srod.compareTo("")!=0) || nieobreal!=0){
					if(nieobreal>3 && nieobreal<50 && nieobreal!=32 && nieobreal!=33){
						//Phrase ppcz1 = new Phrase(""+stout.substring(0,stout.indexOf(":"))+"h "+stout.substring(stout.indexOf(":")+1,stout.length())+"m",sff);
						if(spod.compareTo("")==0 && sch.charAt(i)=='p'){
							if(nieobreal==10)
								stout="08:00";
							else
								stout = userN;
						}
						else if((sch.charAt(i)=='n' || sch.charAt(i)=='s' || sch.charAt(i)=='w') && nieobpl!=0){
							stout="00:00";
						}else if(nieobpl==0 && spod.compareTo("")==0 && (sch.charAt(i)=='n' || sch.charAt(i)=='s' || sch.charAt(i)=='w')){
							stout="00:00";
						}else if(nieobpl>49)
							stout="00:00";
						else
							if(((EcpElement)al.get(i)).getPod().compareTo("")!=0 && ((EcpElement)al.get(i)).getPdo().compareTo("")!=0 && nieobpl==0){
								Time t1 = Time.valueOf(((EcpElement)al.get(i)).getPod()+":00");
								Time t2 = Time.valueOf(((EcpElement)al.get(i)).getPdo()+":00");
								long ltout = t2.getTime() - t1.getTime() - 3600000;
								Time tout = new Time(ltout);
								stout = tout.toString().substring(0,5);
							} 
						//System.out.println(""+nieobreal+"###########2["+i+"] uNominal: >"+stout+"<");
						//System.out.println("###########2["+i+"] uNominal: >"+stout.substring(0,stout.indexOf(":"))+"<    >"+stout.substring(stout.indexOf(":")+1,stout.length()).trim()+"<");
						//System.out.println("STOUT3["+i+"]:"+stout);
						String hstr = new String(stout.substring(0,stout.indexOf(":")));
						String mstr = stout.substring(stout.indexOf(":")+1,stout.length()).trim();
						
						String sstr = ""+Integer.valueOf(hstr)+"h "+Integer.valueOf(mstr)+"m";
						if(cdis==1)
						{
							if(section==0)	
								sstr = sstr+" ("+((EcpElement) al.get(i)).getRcids()+")";
							if(section==2)
								sstr = sstr+" ("+((EcpElement) al.get(i)).getOrcids()+")";
						}	
						//	sstr = sstr+" ("+((EcpElement) al.get(i)).getRcids()+")";
						Phrase ppcz1 = new Phrase(sstr,sff);
						
						//Phrase ppcz1 = new Phrase(""+Integer.valueOf(hstr)+"h "+Integer.valueOf(mstr)+"m",sff);
						
						//Phrase ppcz1 = new Phrase((String)request.getAttribute("userNominal"),sff);
						PdfPCell czcc1 = new PdfPCell(ppcz1);
						czcc1.setHorizontalAlignment(Element.ALIGN_CENTER);
						if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
							czcc1.setBackgroundColor(niedziela);
						else
						if(sch.charAt(i)=='s')
							czcc1.setBackgroundColor(sobota);
						czastable.addCell(czcc1);
					}else if(nieobreal==0){
						if(srod.compareTo(srdo)==0)
							stout="24:00";
						else{
						
						Time t1 = Time.valueOf(srod+":00");
						Time t2 = Time.valueOf(srdo+":00");
						long ltout = t2.getTime() - t1.getTime() - 3600000;
						Time tout = new Time(ltout);
						stout = tout.toString().substring(0,5);
						}
						String sstr = ""+Integer.valueOf(stout.substring(0,stout.indexOf(":")))+"h "+Integer.valueOf(stout.substring(stout.indexOf(":")+1,stout.length()).trim())+"m";
						if(cdis==1){
							if(section==0)	
								sstr = sstr+" ("+((EcpElement) al.get(i)).getRcids()+")";
							if(section==2)
								sstr = sstr+" ("+((EcpElement) al.get(i)).getOrcids()+")";
						}
						//	sstr = sstr+" ("+((EcpElement) al.get(i)).getRcids()+")";
						String UserId = (String)request.getSession().getAttribute("usId");
						String cday = ""+((EcpElement) al.get(i)).getDzien();
						if(cday.length()<2)
							cday="0"+cday;
						HistUserDAO hu = um.getCurrentHistUser(Long.valueOf(UserId) , cday+"-"+myear);
						if(hu!=null && hu.getSysWorkTime().intValue()==5)
							sstr = sstr+" (T)";
						Phrase ppcz1 = new Phrase(sstr,sff);
						
						//Phrase ppcz1 = new Phrase(""+Integer.valueOf(stout.substring(0,stout.indexOf(":")))+"h "+Integer.valueOf(stout.substring(stout.indexOf(":")+1,stout.length()).trim())+"m",sff);
						PdfPCell czcc1 = new PdfPCell(ppcz1);
						czcc1.setHorizontalAlignment(Element.ALIGN_CENTER);
						if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
							czcc1.setBackgroundColor(niedziela);
						else
						if(sch.charAt(i)=='s')
							czcc1.setBackgroundColor(sobota);
						czastable.addCell(czcc1);

					}
					}else{
						Phrase ppcz1 = new Phrase(" ",sff);
						PdfPCell czcc1 = new PdfPCell(ppcz1);
						czcc1.setHorizontalAlignment(Element.ALIGN_CENTER);
						if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
							czcc1.setBackgroundColor(niedziela);
						else
						if(sch.charAt(i)=='s')
							czcc1.setBackgroundColor(sobota);
						czastable.addCell(czcc1);
						
					}
//-----------------------------------------------------------------------------

			}

			document.add(czastable);

			int[] swpo = { 25, 25, 25, 25 };
			PdfPTable sumtable = new PdfPTable(4);
			sumtable.setWidths(swpo);
			sumtable.getDefaultCell().setBorder(1);
			sumtable.getDefaultCell().setPhrase(new Phrase("UTF-8"));
			sumtable.setHorizontalAlignment(0);
			sumtable.setHorizontalAlignment(Element.ALIGN_CENTER);

			Phrase nplan = new Phrase("Planowany czas pracy", sff);
			Phrase nreal = new Phrase("Wypracowany czas pracy", sff);
			Phrase dodat50 = new Phrase("Nadgodziny: dodatek 50%", sff);
			Phrase dodat100 = new Phrase("Nadgodziny: dodatek 100%", sff);
			Phrase normdob = new Phrase("Przekroczenie normy dobowej", sff);
			Phrase normtyg = new Phrase("Przekroczenie normy "+request.getAttribute("sr")+" tyg.", sff);

			
			Phrase ndail = new Phrase("Dzienny czas pracy", sff);
			Phrase nnight = new Phrase("Nocny czas pracy", sff);
			Phrase ncom = new Phrase("Czas pracy dni powszednie", sff);
			Phrase nhol = new Phrase("Czas pracy w dni "
					+ ((String) request.getAttribute("swiateczne")), sff);

			PdfPCell cndail = new PdfPCell(ndail);
			cndail.setHorizontalAlignment(Element.ALIGN_CENTER);
			//sumtable.addCell(cndail);

			PdfPCell cnnight = new PdfPCell(nnight);
			cnnight.setHorizontalAlignment(Element.ALIGN_CENTER);
			//sumtable.addCell(cnnight);

			PdfPCell cncom = new PdfPCell(ncom);
			cncom.setHorizontalAlignment(Element.ALIGN_CENTER);
			//sumtable.addCell(cncom);

			PdfPCell cnhol = new PdfPCell(nhol);
			cnhol.setHorizontalAlignment(Element.ALIGN_CENTER);
			//sumtable.addCell(cnhol);

			ArrayList sumtab = (ArrayList) request.getAttribute("sumtab");

			Phrase nline = new Phrase("\nPODSUMOWANIE", ff);
			PdfPCell cnline = new PdfPCell(nline);
			cnline.setBorder(0);
			cnline.setColspan(4);
			sumtable.addCell(cnline);

			PdfPCell cnplan = new PdfPCell(nplan);
			cnplan.setBackgroundColor(new java.awt.Color(200, 200, 200));
			cnplan.setHorizontalAlignment(Element.ALIGN_CENTER);
			sumtable.addCell(cnplan);

			Phrase ptmp1 = new Phrase(((String) sumtab.get(0)), sff);
			PdfPCell ctmp1 = new PdfPCell(ptmp1);
			ctmp1.setHorizontalAlignment(Element.ALIGN_CENTER);
			sumtable.addCell(ctmp1);

			PdfPCell cnreal = new PdfPCell(nreal);
			cnreal.setBackgroundColor(new java.awt.Color(200, 200, 200));
			cnreal.setHorizontalAlignment(Element.ALIGN_CENTER);
			sumtable.addCell(cnreal);

			Phrase ptmp2 = new Phrase(((String) sumtab.get(1)), sff);
			PdfPCell ctmp2 = new PdfPCell(ptmp2);
			ctmp2.setHorizontalAlignment(Element.ALIGN_CENTER);
			sumtable.addCell(ctmp2);
			
			if(request.getAttribute("kierownik")==null){
				
			PdfPCell cdodat50 = new PdfPCell(dodat50);
			cdodat50.setBackgroundColor(new java.awt.Color(200, 200, 200));
			cdodat50.setHorizontalAlignment(Element.ALIGN_CENTER);
			sumtable.addCell(cdodat50);

			Phrase ptmp50 = new Phrase(((String) request.getAttribute("dod50")), sff);
			PdfPCell ctmp50 = new PdfPCell(ptmp50);
			ctmp50.setHorizontalAlignment(Element.ALIGN_CENTER);
			sumtable.addCell(ctmp50);
			
			PdfPCell cdodat100 = new PdfPCell(dodat100);
			cdodat100.setBackgroundColor(new java.awt.Color(200, 200, 200));
			cdodat100.setHorizontalAlignment(Element.ALIGN_CENTER);
			sumtable.addCell(cdodat100);

			Phrase ptmp100 = new Phrase(((String) request.getAttribute("dod100")), sff);
			PdfPCell ctmp100 = new PdfPCell(ptmp100);
			ctmp100.setHorizontalAlignment(Element.ALIGN_CENTER);
			sumtable.addCell(ctmp100);
			
			}
			PdfPCell cnormdob = new PdfPCell(normdob);
			cnormdob.setBackgroundColor(new java.awt.Color(200, 200, 200));
			cnormdob.setHorizontalAlignment(Element.ALIGN_CENTER);
			//sumtable.addCell(cnormdob);

			Phrase ptmp3 = new Phrase(((String) request.getAttribute("nd")), sff);
			PdfPCell ctmp3 = new PdfPCell(ptmp3);
			ctmp3.setHorizontalAlignment(Element.ALIGN_CENTER);
			//sumtable.addCell(ctmp3);

			PdfPCell cnormtyg = new PdfPCell(normtyg);
			cnormtyg.setBackgroundColor(new java.awt.Color(200, 200, 200));
			cnormtyg.setHorizontalAlignment(Element.ALIGN_CENTER);
			//sumtable.addCell(cnormtyg);

			//Phrase ptmp4 = new Phrase(((String) request.getAttribute("nt")), sff);
			//PdfPCell ctmp4 = new PdfPCell(ptmp4);
			//ctmp4.setHorizontalAlignment(Element.ALIGN_CENTER);
			//sumtable.addCell(ctmp4);

			Phrase nline2 = new Phrase("\n", ff);
			PdfPCell cnline2 = new PdfPCell(nline2);
			cnline2.setBorder(0);
			cnline2.setColspan(4);
			sumtable.addCell(cnline2);

			cndail.setBackgroundColor(new java.awt.Color(180, 180, 180));
			cnnight.setBackgroundColor(new java.awt.Color(180, 180, 180));
			cncom.setBackgroundColor(new java.awt.Color(180, 180, 180));
			cnhol.setBackgroundColor(new java.awt.Color(180, 180, 180));

			sumtable.addCell(cndail);
			sumtable.addCell(cnnight);
			sumtable.addCell(cncom);
			sumtable.addCell(cnhol);

			for (int i = 2; i < 6; i++) {
				Phrase ptmp = new Phrase(((String) sumtab.get(i)), sff);
				PdfPCell ctmp = new PdfPCell(ptmp);
				ctmp.setHorizontalAlignment(Element.ALIGN_CENTER);
				sumtable.addCell(ctmp);

			}

			//Phrase nline = new Phrase("\nPODSUMOWANIE",ff);

			//document.add(nline);
			document.add(sumtable);

			PdfPTable urlopytable = new PdfPTable(3);
			urlopytable.getDefaultCell().setPhrase(new Phrase("UTF-8"));
			//PdfPCell c = new PdfPCell();
			int[] ww = { 65 ,15, 20 };
			urlopytable.setWidths(ww);

			Paragraph pf = new Paragraph("URLOPY", ff);
			PdfPCell s = new PdfPCell(pf);
			s.setBackgroundColor(new java.awt.Color(180, 180, 180));
			s.setColspan(3);
			s.setHorizontalAlignment(Element.ALIGN_CENTER);
			urlopytable.addCell(s);

			ArrayList ot = (ArrayList) request.getAttribute("inne");
			ArrayList tot = (ArrayList) request.getAttribute("tinne");

			Paragraph wypocz = new Paragraph("Wypoczynkowy", sff);
			PdfPCell wyp = new PdfPCell(wypocz);
			Paragraph macierz = new Paragraph((String) request
					.getAttribute("macierz"), sff);
			PdfPCell mac = new PdfPCell(macierz);
			Paragraph wychow = new Paragraph("Wychowawczy", sff);
			PdfPCell wych = new PdfPCell(wychow);
			Paragraph bezplat = new Paragraph((String) request
					.getAttribute("bezplat"), sff);
			PdfPCell bezp = new PdfPCell(bezplat);
			Paragraph okolicz = new Paragraph((String) request
					.getAttribute("okolicz"), sff);
			PdfPCell oko = new PdfPCell(okolicz);
			Paragraph op2dni = new Paragraph("Opieka nad dz.14l (2dni/rok)",
					sff);
			PdfPCell pop2dni = new PdfPCell(op2dni);

			String str = "0";
			Paragraph pwyp = new Paragraph(String.valueOf(((Integer) ot.get(0))
					.intValue()), sff);
			PdfPCell cwyp = new PdfPCell(pwyp);
			cwyp.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph pmac = new Paragraph(String.valueOf(((Integer) ot.get(1))
					.intValue()), sff);
			PdfPCell cmac = new PdfPCell(pmac);
			cmac.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph pwych = new Paragraph(String
					.valueOf(((Integer) ot.get(2)).intValue()), sff);
			PdfPCell cwych = new PdfPCell(pwych);
			cwych.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph pbezp = new Paragraph(String
					.valueOf(((Integer) ot.get(3)).intValue()), sff);
			PdfPCell cbezp = new PdfPCell(pbezp);
			cbezp.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph pokol = new Paragraph(String
					.valueOf(((Integer) ot.get(4)).intValue()), sff);
			PdfPCell cokol = new PdfPCell(pokol);
			cokol.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph popi2dni = new Paragraph(String.valueOf(((Integer) ot
					.get(12)).intValue()), sff);
			PdfPCell copi2dni = new PdfPCell(popi2dni);
			copi2dni.setHorizontalAlignment(Element.ALIGN_CENTER);

			//			cwyp.setHorizontalAlignment(Element.ALIGN_CENTER);

			urlopytable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_CENTER);
			urlopytable.addCell(wyp);
			urlopytable.addCell(cwyp);
			urlopytable.addCell(new Phrase(String.valueOf((String)tot.get(0)),sff));
			
			urlopytable.addCell(mac);
			urlopytable.addCell(cmac);
			urlopytable.addCell(new Phrase(((String)tot.get(1)),sff));

			urlopytable.addCell(wych);
			urlopytable.addCell(cwych);
			urlopytable.addCell(new Phrase(((String)tot.get(2)),sff));

			urlopytable.addCell(bezp);
			urlopytable.addCell(cbezp);
			urlopytable.addCell(new Phrase(((String)tot.get(3)),sff));

			urlopytable.addCell(oko);
			urlopytable.addCell(cokol);
			urlopytable.addCell(new Phrase(((String)tot.get(4)),sff));
			
			urlopytable.addCell(pop2dni);
			urlopytable.addCell(copi2dni);
			urlopytable.addCell(new Phrase(((String)tot.get(12)),sff));

			PdfPTable nieobtable = new PdfPTable(3);
			nieobtable.setWidths(ww);
			nieobtable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_CENTER);

			pf = new Paragraph(((String) request.getAttribute("pozost"))
					.toUpperCase(), ff);
			s = new PdfPCell(pf);
			s.setBackgroundColor(new java.awt.Color(180, 180, 180));
			s.setColspan(3);
			s.setHorizontalAlignment(Element.ALIGN_CENTER);
			nieobtable.addCell(s);

			Paragraph nieplat = new Paragraph(((String) request
					.getAttribute("nieplat"))
					+ " zwolnienie od pracy", sff);
			PdfPCell niepl = new PdfPCell(nieplat);
			Paragraph choroby = new Paragraph("Choroby pracownika", sff);
			PdfPCell chor = new PdfPCell(choroby);
			Paragraph opieka = new Paragraph((String) request
					.getAttribute("opieka2"), sff);
			PdfPCell op = new PdfPCell(opieka);
			Paragraph uspraw = new Paragraph((String) request
					.getAttribute("nu2"), sff);
			PdfPCell uspr = new PdfPCell(uspraw);
			Paragraph nieuspr = new Paragraph(
					"Inne sprawy nieusprawiedliwione", sff);
			PdfPCell nieus = new PdfPCell(nieuspr);
			Paragraph delegacje = new Paragraph("Delegacje", sff);
			PdfPCell deleg = new PdfPCell(delegacje);
			Paragraph inne = new Paragraph("Inne", sff);
			PdfPCell in = new PdfPCell(inne);

			Paragraph pniepl = new Paragraph(String.valueOf(((Integer) ot
					.get(5)).intValue()), sff);
			PdfPCell cniepl = new PdfPCell(pniepl);
			cniepl.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph pchor = new Paragraph(String
					.valueOf(((Integer) ot.get(6)).intValue()), sff);
			PdfPCell cchor = new PdfPCell(pchor);
			cchor.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph popiek = new Paragraph(String.valueOf(((Integer) ot
					.get(7)).intValue()), sff);
			PdfPCell copiek = new PdfPCell(popiek);
			copiek.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph pnu = new Paragraph(String.valueOf(((Integer) ot.get(8))
					.intValue()), sff);
			PdfPCell cnu = new PdfPCell(pnu);
			cnu.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph pnn = new Paragraph(String.valueOf(((Integer) ot.get(9))
					.intValue()), sff);
			PdfPCell cnn = new PdfPCell(pnn);
			cnn.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph pdeleg = new Paragraph(String.valueOf(((Integer) ot
					.get(10)).intValue()), sff);
			PdfPCell cdeleg = new PdfPCell(pdeleg);
			cdeleg.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph pinn = new Paragraph(String
					.valueOf(((Integer) ot.get(11)).intValue()), sff);
			PdfPCell cinn = new PdfPCell(pinn);
			cinn.setHorizontalAlignment(Element.ALIGN_CENTER);

			nieobtable.addCell(niepl);
			nieobtable.addCell(cniepl);
			nieobtable.addCell(new Phrase(((String)tot.get(5)),sff));

			nieobtable.addCell(chor);
			nieobtable.addCell(cchor);
			nieobtable.addCell(new Phrase(((String)tot.get(6)),sff));
			
			nieobtable.addCell(op);
			nieobtable.addCell(copiek);
			nieobtable.addCell(new Phrase(((String)tot.get(7)),sff));
			
			nieobtable.addCell(uspr);
			nieobtable.addCell(cnu);
			nieobtable.addCell(new Phrase(((String)tot.get(8)),sff));
			
			nieobtable.addCell(nieus);
			nieobtable.addCell(cnn);
			nieobtable.addCell(new Phrase(((String)tot.get(9)),sff));
			
			nieobtable.addCell(deleg);
			nieobtable.addCell(cdeleg);
			nieobtable.addCell(new Phrase(((String)tot.get(10)),sff));
			
			nieobtable.addCell(in);
			nieobtable.addCell(cinn);
			nieobtable.addCell(new Phrase(((String)tot.get(11)),sff));
			
			int[] ws = { 40, 60 };

			PdfPTable table = new PdfPTable(2);
			table.setWidths(ws);
			table.getDefaultCell().setBorder(0);
			table.setHorizontalAlignment(0);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);

			table.setTotalWidth((float) 100);
			table.getDefaultCell().setPadding((float) 4);
			Paragraph tyt = new Paragraph("\n\n"
					+ (String) request.getAttribute("desc1"), ff);
			PdfPCell tekst = new PdfPCell(tyt);
			tekst.setColspan(2);
			tekst.setBorder(0);

			table.addCell(tekst);
			table.addCell(urlopytable);
			table.addCell(nieobtable);

			document.add(table);

			int[] wpo = { 50, 50 };
			PdfPTable podpis = new PdfPTable(2);
			podpis.setWidths(wpo);
			podpis.getDefaultCell().setBorder(0);
			podpis.setHorizontalAlignment(0);
			podpis.setHorizontalAlignment(Element.ALIGN_CENTER);

			Paragraph prac = new Paragraph("....................\nPracownik",
					ff);
			Paragraph przel = new Paragraph(".....................\n"
					+ ((String) request.getAttribute("principal")), ff);
			PdfPCell lewy = new PdfPCell(prac);
			lewy.setHorizontalAlignment(Element.ALIGN_LEFT);
			lewy.setBorder(0);

			PdfPCell prawy = new PdfPCell(przel);
			prawy.setHorizontalAlignment(Element.ALIGN_RIGHT);
			prawy.setBorder(0);

			PdfPCell odstep = new PdfPCell(new Phrase("\n\n"));
			odstep.setColspan(2);
			odstep.setBorder(0);

			podpis.addCell(odstep);
			podpis.addCell(lewy);
			podpis.addCell(prawy);

			document.add(podpis);
			ArrayList medSumTab = (ArrayList)request.getAttribute("medSumTab");
			System.out.println("!!!!!!!!!!!!!!1 >"+(String)medSumTab.get(6)+"<  >"+(String)medSumTab.get(7)+"<");
			if((((String)medSumTab.get(6)).trim().compareTo("0h 0m")!=0 || ((String)medSumTab.get(7)).trim().compareTo("0h 0m")!=0)){
			while(document.newPage()==false)
				document.add(new Paragraph("\n"));
		
			PdfPTable title2table = new PdfPTable(2);
			
			title2table.setWidths(wids);
			title2table.setWidthPercentage((float)98);
			Paragraph _p = new Paragraph(((String)request.getAttribute("repName")).trim().toUpperCase()+"\n",ffb10);
			Paragraph pmed = new Paragraph((String)request.getAttribute("MEDTEL"),fbold);
			_p.add(pmed);
			_p.add(p1);
			_p.setAlignment("center");
			
			PdfPCell c2p = new PdfPCell(_p);
			c2p.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2p.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c2p.setBorder(0);
			
			
			title2table.addCell(c2p);
			title2table.addCell(cgif);
			
			document.add(title2table);
			
			practable.addCell(nrewicell);
			practable.addCell(miescell);
			practable.addCell(cnline2);
			document.add(practable);
			
			
			PdfPTable czas2table = new PdfPTable(7);
			czas2table.getDefaultCell().setPhrase(new Phrase("UTF-8"));
			czas2table.getDefaultCell().setPaddingBottom(0);
			czas2table.getDefaultCell().setPaddingLeft(0);
			czas2table.getDefaultCell().setPaddingRight(0);
			czas2table.getDefaultCell().setPaddingTop(0);
			
			czas2table.setWidths(w);
			
			czas2table.addCell(lpp);
			
			
			gotow.setColspan(3);
			czas2table.addCell(gotow);
			
			dyzur.setColspan(3);
			czas2table.addCell(dyzur);

			czas2table.addCell("");
			czas2table.addCell(odc);
			czas2table.addCell(doc);
			czas2table.addCell(czc);
			czas2table.addCell(odc);
			czas2table.addCell(doc);
			czas2table.addCell(czc);
			int dyrtel=0, wezwil=0;
			for (int i = 0; i < al.size(); i++) {
				if(((EcpElement) al.get(i)).getTod().trim().compareTo("")!=0 || ((EcpElement) al.get(i)).getMod().trim().compareTo("")!=0){
				String slp = String.valueOf(i + 1);
				if(sch.charAt(i)=='n')
					slp += "(N)";

				plp = new Phrase(slp, sff);
				cplp = new PdfPCell(plp);
				cplp.setHorizontalAlignment(Element.ALIGN_CENTER);
			
				if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
					cplp.setBackgroundColor(niedziela);
				else
				if(sch.charAt(i)=='s')
					cplp.setBackgroundColor(sobota);
				czas2table.addCell(cplp);
				
				Phrase tod = new Phrase(((EcpElement) al.get(i)).getTod(), sff);
				PdfPCell ctod = new PdfPCell(tod);
				ctod.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
					ctod.setBackgroundColor(niedziela);
				else
				if(sch.charAt(i)=='s')
					ctod.setBackgroundColor(sobota);
				czas2table.addCell(ctod);

				Phrase tdo = new Phrase(((EcpElement) al.get(i)).getTdo(), sff);
				PdfPCell ctdo = new PdfPCell(tdo);
				ctdo.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
					ctdo.setBackgroundColor(niedziela);
				else
				if(sch.charAt(i)=='s')
					ctdo.setBackgroundColor(sobota);
				czas2table.addCell(ctdo);

				String twyn=" ";
				if(((EcpElement) al.get(i)).getTod().compareTo(((EcpElement) al.get(i)).getTdo())==0 && ((EcpElement) al.get(i)).getTod().compareTo("")!=0){
					twyn="24h 0m";
					dyrtel++;
					if(dblcount==1 && (sch.charAt(i)=='n' || sch.charAt(i)=='w'))
						dyrtel++;
				}else
				if(((EcpElement) al.get(i)).getTod().compareTo("")!=0){
					Time t1 = Time.valueOf(((EcpElement) al.get(i)).getTod()+":00");
					Time t2 = Time.valueOf(((EcpElement) al.get(i)).getTdo()+":00");
					long ltout = t2.getTime() - t1.getTime() - 3600000;
					Time tout = new Time(ltout);
					String tstout = tout.toString().substring(0,5);
					twyn = ""+Integer.valueOf(tstout.substring(0,2)).intValue()+"h "+Integer.valueOf(tstout.substring(3,5)).intValue()+"m";
					dyrtel++;
				}
				Phrase ppcz11 = new Phrase(twyn,sff);
				PdfPCell czcc11 = new PdfPCell(ppcz11);
				czcc11.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
					czcc11.setBackgroundColor(niedziela);
				else
				if(sch.charAt(i)=='s')
					czcc11.setBackgroundColor(sobota);
				czas2table.addCell(czcc11);
				
				Phrase mod = new Phrase(((EcpElement) al.get(i)).getMod(), sff);
				PdfPCell cmod = new PdfPCell(mod);
				cmod.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
					cmod.setBackgroundColor(niedziela);
				else
				if(sch.charAt(i)=='s')
					cmod.setBackgroundColor(sobota);
				czas2table.addCell(cmod);

				Phrase mdo = new Phrase(((EcpElement) al.get(i)).getMdo(), sff);
				PdfPCell cmdo = new PdfPCell(mdo);
				cmdo.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
					cmdo.setBackgroundColor(niedziela);
				else
				if(sch.charAt(i)=='s')
					cmdo.setBackgroundColor(sobota);
				czas2table.addCell(cmdo);

				String mwyn=" ";
				if(((EcpElement) al.get(i)).getMod().compareTo(((EcpElement) al.get(i)).getMdo())==0 && ((EcpElement) al.get(i)).getMod().compareTo("")!=0){
					mwyn="24h 0m";
					wezwil++;
				}else
				if(((EcpElement) al.get(i)).getMod().compareTo("")!=0){
					Time t1 = Time.valueOf(((EcpElement) al.get(i)).getMod()+":00");
					Time t2 = Time.valueOf(((EcpElement) al.get(i)).getMdo()+":00");
					long ltout = t2.getTime() - t1.getTime() - 3600000;
					Time tout = new Time(ltout);
					String tstout = tout.toString().substring(0,5);
					mwyn = ""+Integer.valueOf(tstout.substring(0,2)).intValue()+"h "+Integer.valueOf(tstout.substring(3,5)).intValue()+"m";
					wezwil++;
				}
				Phrase ppcz12 = new Phrase(mwyn,sff);
				PdfPCell czcc12 = new PdfPCell(ppcz12);
				czcc12.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(sch.charAt(i)=='n' || sch.charAt(i)=='w')
					czcc12.setBackgroundColor(niedziela);
				else
				if(sch.charAt(i)=='s')
					czcc12.setBackgroundColor(sobota);
				czas2table.addCell(czcc12);
				
				}
			}
			
			document.add(czas2table);
			Paragraph copyr = new Paragraph((String)request.getAttribute("copyright"),ssff);
            copyr.setAlignment("center");
            
            document.add(copyr);
			
			PdfPTable pmedTab = new PdfPTable(3);
			float[] tmw = {(float)30,(float)35,(float)35};
			pmedTab.setWidths(tmw);
			cnline2.setColspan(3);
			pmedTab.addCell(cnline2);
			
			//Paragraph pauza = new Paragraph("RODZAJ",ff);
			Paragraph pauza = new Paragraph(" ",ff);
			PdfPCell cpauza = new PdfPCell(pauza);
			cpauza.setHorizontalAlignment(Element.ALIGN_CENTER);
			cpauza.setBackgroundColor(niedziela);
			pmedTab.addCell(cpauza);
			
			Paragraph titWez = new Paragraph("DYŻUR TELEFONICZNY",ff);
			titWez.setAlignment("center");
			PdfPCell pWez = new PdfPCell(titWez);
			pWez.setHorizontalAlignment(Element.ALIGN_CENTER);
			pWez.setBackgroundColor(niedziela);
			pmedTab.addCell(pWez);
			
			Paragraph titDyz = new Paragraph("WEZWANIA",ff);
			titDyz.setAlignment("center");
			PdfPCell pDyz = new PdfPCell(titDyz);
			pDyz.setHorizontalAlignment(Element.ALIGN_CENTER);
			pDyz.setBackgroundColor(niedziela);
			pmedTab.addCell(pDyz);
			
			Paragraph titNoc = new Paragraph("Czas w porze nocnej:",sff);
			Paragraph titDzien = new Paragraph((String)request.getAttribute("IL_DZIEN"),sff);
			Paragraph titSw = new Paragraph((String)request.getAttribute("IL_SW"),sff);
			Paragraph titRazem = new Paragraph("RAZEM:",sff);
			titNoc.setAlignment("right");
			titDzien.setAlignment("right");
			titSw.setAlignment("right");
			titRazem.setAlignment("right");
			
			
			PdfPCell ctitNoc = new PdfPCell(titNoc);
			ctitNoc.setHorizontalAlignment(Element.ALIGN_RIGHT);
			PdfPCell ctitDzien = new PdfPCell(titDzien);
			ctitDzien.setHorizontalAlignment(Element.ALIGN_RIGHT);
			PdfPCell ctitSw = new PdfPCell(titSw);
			ctitSw.setHorizontalAlignment(Element.ALIGN_RIGHT);
			PdfPCell ctitRazem = new PdfPCell(titRazem);
			ctitRazem.setHorizontalAlignment(Element.ALIGN_RIGHT);
			ctitRazem.setBorderWidth((float)1);
			
			pmedTab.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			//ArrayList medSumTab = (ArrayList)request.getAttribute("medSumTab");
			
			//pmedTab.addCell(ctitDzien);
			PlanManager mgr = new PlanManager();
			String plus=mgr.addTimes((String)medSumTab.get(0),(String)medSumTab.get(8));
			//pmedTab.addCell(new Paragraph(plus,sff));
			//pmedTab.addCell(new Paragraph((String)medSumTab.get(3),sff));
			
			//pmedTab.addCell(ctitNoc);
			//pmedTab.addCell(new Paragraph((String)medSumTab.get(1),sff));
			//pmedTab.addCell(new Paragraph((String)medSumTab.get(4),sff));
			
			//pmedTab.addCell(ctitSw);
			//pmedTab.addCell(new Paragraph((String)medSumTab.get(2),sff));
			//pmedTab.addCell(new Paragraph((String)medSumTab.get(5),sff));
			
			pmedTab.addCell(ctitRazem);
			//String minus = mgr.addTimes((String)medSumTab.get(6),"-"+(String)medSumTab.get(8));
			//minus = mgr.addTimes(plus,(String)medSumTab.get(1));
			//minus = mgr.addTimes(minus,(String)medSumTab.get(2));
			String minus=""+dyrtel;
			PdfPCell r1 = new PdfPCell(new Paragraph(minus,sff));
			r1.setBorderWidth((float)1);
			r1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pmedTab.addCell(r1);
			//PdfPCell r2 = new PdfPCell(new Paragraph((String)medSumTab.get(7),sff));
			PdfPCell r2 = new PdfPCell(new Paragraph(""+wezwil,sff));
			r2.setBorderWidth((float)1);
			r2.setHorizontalAlignment(Element.ALIGN_CENTER);
			pmedTab.addCell(r2);
			
			
			document.add(pmedTab);
			
			document.add(odstep);
			
			document.add(podpis);
			//document.add(new Paragraph("NOWA STRONA......"));
			// step 5: we close the document
			}
			document.close();

			// step 6: we output the writer as bytes to the response output
			DataOutput output = new DataOutputStream(response.getOutputStream());
			byte[] bytes = buffer.toByteArray();
			response.setContentLength(bytes.length);
			for (int i = 0; i < bytes.length; i++) {
				output.writeByte(bytes[i]);
			}
		%>