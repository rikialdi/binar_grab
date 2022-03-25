<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Java Techie Mail</title>
</head>

<body style="font-family: 'Space Grotesk';font-size: 12px">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
    <tr >
        <td align="center" valign="top" bgcolor="#FFFFFF" style="background-color: #edecea;  "><br> <br>

            <table width="50%" border="0" cellspacing="0" cellpadding="0"  >
                <tr >
                    <td valign="top" bgcolor="#FFFFFF" align="center"
                        style="background-color: #FFFFFF;     font-size: 12px; color: #000000; padding: 20px;">
                        <div style="">
                            <img src="${chuteicon}" style="width:80px;height:80px;">
                        </div>
                    </td>
                </tr>
                <tr >
                    <td valign="top" bgcolor="#FFFFFF"
                        style="background-color: #FFFFFF;   font-size: 12px; color: #000000; padding: 0px 50px 0px 50px; ">


                        <div style="font-size: 12px; color: #000000;">
                            <b>Dear,${namesaya}</b> <br>
                            <br> Your order confirmation from:
                            <table><tr>
                                    <td width="20"><img src="${iconuser}" alt="Avatar" style="width:30px;height:30px;  border-radius: 50%"></td>
                                    <td><b>Jl Sudirman</b></td>
                                </tr></table><br>

                        </div>
                        <table style="border-bottom:2px dashed  ">
                            <tr >
                                <th style="border-bottom:2px dashed ">Qty</th>
                                <th style="border-bottom:2px dashed  ">Desc</th>
                                <th style="border-bottom:2px dashed  ">Amt</th>
                            </tr>
                            <#list datainvoice as item>
                                <tr>
                                    <td>${item.nama}</td>
                                    <td>${item.satuan}</td>
                                    <td>${item.harga}</td>
                                </tr>
                                <tr>
                                    <td colspan="1">1</td>
                                    <td colspan="1">Shipping</td>
                                    <td colspan="1">10</td>
                                </tr>
                                <br>
                            </#list>
                            <tr>
                                <td colspan="2" style="text-align: right;">SUBTOTAL</td>
                                <td colspan="1" >10000</td>
                            </tr>

                        </table>
                        <div style="font-size: 12px; color: #000000;">
                            <br> <b>Will be shipped to:</b><br>
                            Jl Sudirman <br>
                            Jakarta <br>
                            20876 <br>
                        </div>
                        <div style="font-size: 12px; color: #000000;">
                            <br> <li>You ill get notification on our app <b>@UserTes</b> when ship
                                your items.</li><br>

                        </div>

                    </td>
                </tr>
            </table>
            <br> <br></td>
    </tr>
</table>
</body>

</html>