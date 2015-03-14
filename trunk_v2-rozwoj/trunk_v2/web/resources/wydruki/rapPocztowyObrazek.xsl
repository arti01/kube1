<?xml version='1.0' encoding='UTF-8' ?> 
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:barcode="org.krysalis.barcode4j.xalan.BarcodeExt" xmlns:common="http://exslt.org/common"
                xmlns:xalan="http://xml.apache.org" exclude-result-prefixes="barcode common xalan" xmlns:rx="http://www.renderx.com/XSL/Extensions">
    <xsl:template match="DcDokPocztaList">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-landscape"  margin-left="0.5cm" margin-right="0.5cm" page-height="21cm" page-width="29.7cm">
                    <fo:region-body margin-top="0.5cm" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4-landscape">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-family="Arial" font-size="11pt" font-weight="normal">
                        <fo:table>
                            <fo:table-column column-number="1" column-width="1cm" />
                            <fo:table-column column-number="2" column-width="4cm" />
                            <fo:table-column column-number="3" column-width="4cm" />
                            <fo:table-column column-number="4" column-width="2cm" />
                            <fo:table-column column-number="5" column-width="1cm" />
                            <fo:table-column column-number="6" column-width="1cm" />
                            <fo:table-column column-number="7" column-width="1cm" />
                            <fo:table-column column-number="8" column-width="4cm" />
                            <fo:table-column column-number="9" column-width="4cm" />
                            <fo:table-column column-number="10" column-width="2cm" />
                            <fo:table-column column-number="11" column-width="1cm" />
                            <fo:table-column column-number="12" column-width="2cm" />
                            <fo:table-column column-number="13" column-width="1cm" />

                            <fo:table-header>
                                <fo:table-row border-bottom="solid">
                                    <fo:table-cell number-columns-spanned="13">
                                        <fo:block font-family="Arial" font-weight="bold"  text-align="right" margin-right="0cm">Od:<xsl:value-of select="./dataOd" /> Do:<xsl:value-of select="./dataDo" /></fo:block>
                                        <fo:block font-family="Arial" font-weight="bold" margin-top="1cm" margin-bottom="1cm">Nazwa oraz adres Nadawcy</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>                            
                                <fo:table-row>
                                    <fo:table-cell text-align="center" margin-left="0.2cm" border-left="solid">
                                        <fo:block>lp.
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" margin-left="0.2cm" border-left="solid">
                                        <fo:block>ADRESAT</fo:block>
                                        <fo:block>(imię i nazwisko lub nazwa)</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" margin-left="0.2cm" border-left="solid">
                                        <fo:block>Dokładne miejsce doręczenia</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" number-columns-spanned="2">
                                        <fo:block>Kwota zadekl. wartości</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" number-columns-spanned="2">
                                        <fo:block>Zadekl. waga</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" margin-left="0.2cm" border-left="solid">
                                        <fo:block>Nr nadawczy</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" margin-left="0.2cm" border-left="solid">
                                        <fo:block>Uwagi</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" number-columns-spanned="2">
                                        <fo:block>Opłata</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" number-columns-spanned="2">
                                        <fo:block>Kwota pobrania</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell text-align="center" margin-left="0.2cm" border-left="solid">
                                        <fo:block></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center"  margin-left="0.2cm" border-left="solid">
                                        <fo:block></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center"  border-left="solid">
                                        <fo:block></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                        <fo:block>zł</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                        <fo:block>gr</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                        <fo:block>kg</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                        <fo:block>g</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center"  margin-left="0.2cm" border-left="solid">
                                        <fo:block></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center"  margin-left="0.2cm" border-left="solid">
                                        <fo:block></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                        <fo:block>zł</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                        <fo:block>gr</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                        <fo:block>zł</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                        <fo:block>gr</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>                           
                            
                            <fo:table-body>
                                <xsl:for-each select="./dokumentList/dcDokPoczta">
                                    <fo:table-row border="solid 0.1mm black">
                                        <fo:table-cell text-align="right" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="id" />.
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="kontrahentNazwa" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="kontrahentAdres" />
                                                <xsl:param name="imageVar"><xsl:value-of select="obraz"/></xsl:param>
                                                <fo:external-graphic  src="url('data:image/jpeg;base64,{$imageVar}')" content-height="0.5cm" content-width="1cm"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                                <xsl:for-each select="./dokumentList/dcDokPoczta">
                                    <fo:table-row border="solid 0.1mm black">
                                        <fo:table-cell text-align="right" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="id" />.
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="kontrahentNazwa" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="kontrahentAdres" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                                <xsl:for-each select="./dokumentList/dcDokPoczta">
                                    <fo:table-row border="solid 0.1mm black">
                                        <fo:table-cell text-align="right" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="id" />.
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="kontrahentNazwa" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="kontrahentAdres" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                                <xsl:for-each select="./dokumentList/dcDokPoczta">
                                    <fo:table-row border="solid 0.1mm black">
                                        <fo:table-cell text-align="right" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="id" />.
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="kontrahentNazwa" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block>
                                                <xsl:value-of select="kontrahentAdres" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" border="solid 0.3mm black" margin-left="0.2cm">
                                            <fo:block></fo:block>
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