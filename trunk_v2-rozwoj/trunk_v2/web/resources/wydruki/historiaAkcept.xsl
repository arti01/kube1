<?xml version='1.0' encoding='UTF-8' ?> 
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:barcode="org.krysalis.barcode4j.xalan.BarcodeExt" xmlns:common="http://exslt.org/common"
                xmlns:xalan="http://xml.apache.org" exclude-result-prefixes="barcode common xalan" xmlns:rx="http://www.renderx.com/XSL/Extensions">
    <xsl:template match="DcDokPoczta">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-portrait"  margin-left="0.5cm" margin-right="0.5cm" page-height="29.7cm" page-width="21cm">
                    <fo:region-body margin-top="0.5cm" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4-portrait">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>data wydruku: <xsl:value-of select="dataWydruku" /></fo:block>
                    <fo:block font-family="Arial" font-size="13pt" font-weight="normal" linefeed-treatment="preserve">
                        Nazwa dokumentu: <xsl:value-of select="nazwa" />
                    </fo:block>
                    <fo:block>&#0160;</fo:block>
                    <fo:block font-family="Arial" font-size="11pt" font-weight="normal" linefeed-treatment="preserve">
                        <fo:table>
                            <fo:table-column column-number="1" column-width="1cm" />
                            <fo:table-column column-number="2" column-width="5cm" />
                            <fo:table-column column-number="3" column-width="3cm" />
                            <fo:table-column column-number="4" column-width="10cm" />
                            
                            <fo:table-header>                                
                                <fo:table-row>
                                    <fo:table-cell text-align="center" margin-left="0.2cm" >
                                        <fo:block>LP</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" margin-left="0.2cm" >
                                        <fo:block>Typ akceptacji</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" margin-left="0.2cm" >
                                        <fo:block>Status</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" margin-left="0.2cm" >
                                        <fo:block>Szczegóły</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>                           
                            
                            
                            <fo:table-body>
                                <xsl:for-each select="./krokList/dcDokKrokWyd">
                                    <fo:table-row border="solid 0.1mm black">
                                        <fo:table-cell text-align="right" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="lp" />.
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="typNazwa" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="status" />
                                            </fo:block>
                                        </fo:table-cell>
                                        
                                        <fo:table-cell text-align="left" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <fo:table>
                                                    <fo:table-column column-number="1" column-width="9.5cm" />
                                                    <fo:table-body>
                                                        <xsl:for-each select="KrokiUserList/krokUser">
                                                            <fo:table-row border-bottom ="solid 0.1mm black">
                                                                <fo:table-cell text-align="left" border="solid 0.0mm black" margin-left="0.2cm">
                                                                    <fo:block>
                                                                        <xsl:value-of select="fullname" />
                                                                    </fo:block>                        
                                                                    <fo:block>
                                                                        <xsl:value-of select="akcept" />:
                                                                        <xsl:value-of select="data" />
                                                                    </fo:block>
                                                                    <fo:block font-weight="bold">
                                                                       <xsl:value-of select="info" />
                                                                  </fo:block>
                                                                </fo:table-cell>
                                                            </fo:table-row>
                                                        </xsl:for-each>
                                                    </fo:table-body>
                                                </fo:table>
                                            </fo:block>
                                        </fo:table-cell>
                                        
                                    </fo:table-row>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
 
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>