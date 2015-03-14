<?xml version='1.0' encoding='UTF-8' ?> 
<!-- was: <?xml version="1.0" encoding="iso-8859-1"?> -->
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:barcode="org.krysalis.barcode4j.xalan.BarcodeExt" xmlns:common="http://exslt.org/common"
                xmlns:xalan="http://xml.apache.org" exclude-result-prefixes="barcode common xalan">
    <xsl:template match="DcDokPocztaList">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simple" page-height="20cm" page-width="10.5cm" margin-left="0.2cm" margin-right="0.2cm">
                    <fo:region-body margin-top="0.5cm" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simple">
                <fo:flow flow-name="xsl-region-body">
 <fo:block font-family="Arial" font-weight="bold">Dokumnty do wysyŁłki</fo:block>
                    <fo:block font-family="Arial" font-size="7pt" font-weight="normal">
                        <fo:table>
                            <fo:table-column column-number="1" column-width="3cm" />
                            <fo:table-column column-number="2" column-width="2cm" />
                            <fo:table-column column-number="3" column-width="5cm" />
                            <fo:table-body  height="10cm">
                                <xsl:for-each select="./dokumentList/dcDokPoczta">
                                    <fo:table-row border="solid 0.1mm black">
 
 
                                        <fo:table-cell text-align="left">
                                            <fo:block>
                                                <xsl:value-of select="id" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="left">
                                            <fo:block>
                                                <xsl:value-of select="nazwa" />
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