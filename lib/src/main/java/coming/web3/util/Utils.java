package coming.web3.util;


import static coming.web3.ethereum.EthereumNetworkBase.AVALANCHE_ID;
import static coming.web3.ethereum.EthereumNetworkBase.BINANCE_MAIN_ID;
import static coming.web3.ethereum.EthereumNetworkBase.CLASSIC_ID;
import static coming.web3.ethereum.EthereumNetworkBase.MAINNET_ID;
import static coming.web3.ethereum.EthereumNetworkBase.MATIC_ID;
import static coming.web3.ethereum.EthereumNetworkBase.OPTIMISTIC_MAIN_ID;
import static coming.web3.ethereum.EthereumNetworkBase.POA_ID;
import static coming.web3.ethereum.EthereumNetworkBase.XDAI_ID;

import android.content.Context;
import android.content.pm.InstallSourceInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;

import androidx.annotation.RawRes;

import org.jetbrains.annotations.NotNull;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String ISOLATE_NUMERIC = "(0?x?[0-9a-fA-F]+)";
    private static final String ICON_REPO_ADDRESS_TOKEN = "[TOKEN]";
    private static final String CHAIN_REPO_ADDRESS_TOKEN = "[CHAIN]";
    private static final String TOKEN_LOGO = "/logo.png";
    public  static final String ALPHAWALLET_REPO_NAME = "https://raw.githubusercontent.com/alphawallet/iconassets/master/";
    private static final String TRUST_ICON_REPO_BASE = "https://raw.githubusercontent.com/trustwallet/assets/master/blockchains/";
    private static final String TRUST_ICON_REPO = TRUST_ICON_REPO_BASE + CHAIN_REPO_ADDRESS_TOKEN + "/assets/" + ICON_REPO_ADDRESS_TOKEN + TOKEN_LOGO;
    private static final String ALPHAWALLET_ICON_REPO = ALPHAWALLET_REPO_NAME + ICON_REPO_ADDRESS_TOKEN + TOKEN_LOGO;

    public static int dp2px(Context context, int dp) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }


    public static boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }

    public static boolean isAlNum(String testStr)
    {
        boolean result = false;
        if (testStr != null && testStr.length() > 0)
        {
            result = true;
            for (int i = 0; i < testStr.length(); i++)
            {
                char c = testStr.charAt(i);
                if (!Character.isIdeographic(c) && !Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && (c < 32 || c > 126))
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    public static boolean isValidValue(String testStr)
    {
        boolean result = false;
        if (testStr != null && testStr.length() > 0)
        {
            result = true;
            for (int i = 0; i < testStr.length(); i++)
            {
                char c = testStr.charAt(i);
                if (!Character.isDigit(c) && !(c == '.' || c == ','))
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    private static String getFirstWord(String text) {
        if (TextUtils.isEmpty(text)) return "";
        text = text.trim();
        int index;
        for (index = 0; index < text.length(); index++)
        {
            if (!Character.isLetterOrDigit(text.charAt(index))) break;
        }

        return text.substring(0, index).trim();
    }

    public static String getIconisedText(String text)
    {
        if (TextUtils.isEmpty(text)) return "";
        if (text.length() <= 4) return text;
        String firstWord = getFirstWord(text);
        if (!TextUtils.isEmpty(firstWord))
        {
            return firstWord.substring(0, Math.min(firstWord.length(), 4)).toUpperCase();
        }
        else
        {
            return "";
        }
    }

    public static String getShortSymbol(String text)
    {
        if (TextUtils.isEmpty(text)) return "";
        String firstWord = getFirstWord(text);
        if (!TextUtils.isEmpty(firstWord))
        {
            return firstWord.substring(0, Math.min(firstWord.length(), 5)).toUpperCase();
        }
        else
        {
            return "";
        }
    }


    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static boolean isAddressValid(String address)
    {
        return address != null && address.length() > 0 && WalletUtils.isValidAddress(address);
    }

    public static String longArrayToString(Long[] values)
    {
        StringBuilder store = new StringBuilder();
        boolean firstValue = true;
        for (long network : values)
        {
            if (!firstValue) store.append(",");
            store.append(network);
            firstValue = false;
        }

        return store.toString();
    }

    public static List<Long> longListToArray(String list)
    {
        List<Long> idList = new ArrayList<>();
        //convert to array
        String[] split = list.split(",");
        for (String s : split)
        {
            Long value;
            try
            {
                value = Long.valueOf(s);
                idList.add(value);
            }
            catch (NumberFormatException e)
            {
                //empty
            }
        }

        return idList;
    }

    public static int[] bigIntegerListToIntList(List<BigInteger> ticketSendIndexList)
    {
        int[] indexList = new int[ticketSendIndexList.size()];
        for (int i = 0; i < ticketSendIndexList.size(); i++) indexList[i] = ticketSendIndexList.get(i).intValue();
        return indexList;
    }

    public static BigInteger parseTokenId(String tokenIdStr)
    {
        BigInteger tokenId;
        try
        {
            tokenId = new BigInteger(tokenIdStr);
        }
        catch (Exception e)
        {
            tokenId = BigInteger.ZERO;
        }

        return tokenId;
    }

    /**
     * Produce a string CSV of integer IDs given an input list of values
     * @param idList
     * @param keepZeros
     * @return
     */
    public static String bigIntListToString(List<BigInteger> idList, boolean keepZeros)
    {
        if (idList == null) return "";
        String displayIDs = "";
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (BigInteger id : idList)
        {
            if (!keepZeros && id.compareTo(BigInteger.ZERO) == 0) continue;
            if (!first)
            {
                sb.append(",");
            }
            first = false;

            sb.append(Numeric.toHexStringNoPrefix(id));
            displayIDs = sb.toString();
        }

        return displayIDs;
    }

    public static List<Integer> stringIntsToIntegerList(String userList)
    {
        List<Integer> idList = new ArrayList<>();

        try
        {
            String[] ids = userList.split(",");

            for (String id : ids)
            {
                //remove whitespace
                String trim = id.trim();
                Integer intId = Integer.parseInt(trim);
                idList.add(intId);
            }
        }
        catch (Exception e)
        {
            idList = new ArrayList<>();
        }

        return idList;
    }

    public static String integerListToString(List<Integer> intList, boolean keepZeros)
    {
        if (intList == null) return "";
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (Integer id : intList)
        {
            if (!keepZeros && id == 0) continue;
            if (!first)sb.append(",");
            sb.append(id);
            first = false;
        }

        return sb.toString();
    }

    public static Map<BigInteger, BigInteger> getIdMap(List<BigInteger> tokenIds)
    {
        Map<BigInteger, BigInteger> tokenMap = new HashMap<>();
        for (BigInteger tokenId : tokenIds)
        {
            tokenMap.put(tokenId, tokenMap.containsKey(tokenId) ? tokenMap.get(tokenId).add(BigInteger.ONE) : BigInteger.ONE);
        }

        return tokenMap;
    }

    public static boolean isNumeric(String numString)
    {
        if (numString == null || numString.length() == 0) return false;

        for (int i = 0; i < numString.length(); i++)
        {
            if (Character.digit(numString.charAt(i), 10) == -1) { return false; }
        }

        return true;
    }

    public static boolean isHex(String hexStr)
    {
        if (hexStr == null || hexStr.length() == 0) return false;
        hexStr = Numeric.cleanHexPrefix(hexStr);

        for (int i = 0; i < hexStr.length(); i++)
        {
            if (Character.digit(hexStr.charAt(i), 16) == -1) { return false; }
        }

        return true;
    }

    public static String isolateNumeric(String valueFromInput)
    {
        try
        {
            Matcher regexResult = Pattern.compile(ISOLATE_NUMERIC).matcher(valueFromInput);
            if (regexResult.find())
            {
                if (regexResult.groupCount() >= 1)
                {
                    valueFromInput = regexResult.group(0);
                }
            }
        }
        catch (Exception e)
        {
            // Silent fail - no action; just return input; this function is only to clean junk from a number
        }

        return valueFromInput;
    }

    public static String formatAddress(String address) {
        if (isAddressValid(address))
        {
            address = Keys.toChecksumAddress(address);
            String result = "";
            String firstSix = address.substring(0, 6);
            String lastSix = address.substring(address.length() - 4);
            return result + firstSix + "..." + lastSix;
        }
        else
        {
            return "0x";
        }
    }

    /**
     * Just enough for diagnosis of most errors
     * @param s String to be HTML escaped
     * @return escaped string
     */
    public static String escapeHTML(String s) {
        StringBuilder out = new StringBuilder(Math.max(16, s.length()));
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c)
            {
                case '"':
                    out.append("&quot;");
                    break;
                case '&':
                    out.append("&amp;");
                    break;
                case '<':
                    out.append("&lt;");
                    break;
                case '>':
                    out.append("&gt;");
                    break;
                default:
                    out.append(c);
            }
        }
        return out.toString();
    }

    public static long randomId() {
        return new Date().getTime();
    }

    public static String getDomainName(String url)
    {
        try
        {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        }
        catch (Exception e)
        {
            return url != null ? url : "";
        }
    }

    @NotNull
    public static String getTokenAddrFromUrl(String url)
    {
        if (!TextUtils.isEmpty(url) && url.startsWith(TRUST_ICON_REPO_BASE))
        {
            int start = url.lastIndexOf("/assets/") + "/assets/".length();
            int end = url.lastIndexOf(TOKEN_LOGO);
            if (start > 0 && end > 0)
            {
                return url.substring(start, end);
            }
        }

        return "";
    }

    @NotNull
    public static String getTokenAddrFromAWUrl(String url)
    {
        if (!TextUtils.isEmpty(url) && url.startsWith(ALPHAWALLET_REPO_NAME))
        {
            int start = ALPHAWALLET_REPO_NAME.length();
            int end = url.lastIndexOf(TOKEN_LOGO);
            if (end > 0 && end > start)
            {
                return url.substring(start, end);
            }
        }

        return "";
    }

    private static final Map<Long, String> twChainNames = new HashMap<Long, String>() {
        {
            put(CLASSIC_ID, "classic");
            put(XDAI_ID, "xdai");
            put(POA_ID, "poa");
            put(BINANCE_MAIN_ID, "smartchain");
            put(AVALANCHE_ID, "avalanche");
            put(OPTIMISTIC_MAIN_ID, "optimism");
            put(MATIC_ID, "polygon");
            put(MAINNET_ID, "ethereum");
        }
    };

    @NotNull
    public static String getTWTokenImageUrl(long chainId, String address)
    {
        String tURL = TRUST_ICON_REPO;
        String repoChain = twChainNames.get(chainId);
        if (repoChain == null) repoChain = "ethereum";
        tURL = tURL.replace(ICON_REPO_ADDRESS_TOKEN, address).replace(CHAIN_REPO_ADDRESS_TOKEN, repoChain);
        return tURL;
    }

    @NotNull
    public static String getTokenImageUrl(String address)
    {
        return ALPHAWALLET_ICON_REPO.replace(ICON_REPO_ADDRESS_TOKEN, Keys.toChecksumAddress(address));
    }

    public static String getAWIconRepo(String address)
    {
        return ALPHAWALLET_ICON_REPO.replace(ICON_REPO_ADDRESS_TOKEN, Keys.toChecksumAddress(address));
    }


    private static final String IPFS_PREFIX = "ipfs://";

    public static String parseIPFS(String URL)
    {
        if (TextUtils.isEmpty(URL)) return URL;
        String parsed = URL;
        int ipfsIndex = URL.lastIndexOf("/ipfs/");
        if (ipfsIndex >= 0)
        {
            parsed = "https://ipfs.io" + URL.substring(ipfsIndex);
        }
        else if (URL.startsWith(IPFS_PREFIX))
        {
            parsed = "https://ipfs.io/ipfs/" + URL.substring(IPFS_PREFIX.length());
        }

        return parsed;
    }

    public static String loadFile(Context context, @RawRes int rawRes) {
        byte[] buffer = new byte[0];
        try {
            InputStream in = context.getResources().openRawResource(rawRes);
            buffer = new byte[in.available()];
            int len = in.read(buffer);
            if (len < 1) {
                throw new IOException("Nothing is read.");
            }
        } catch (Exception ex) {
            Log.d("READ_JS_TAG", "Ex", ex);
        }
        return new String(buffer);
    }

    public static long timeUntil(long eventInMillis)
    {
        return eventInMillis - System.currentTimeMillis();
    }

    //TODO: detect various App Library installs and re-direct appropriately
    public static boolean verifyInstallerId(Context context) {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            String installingPackageName;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                final InstallSourceInfo installer = packageManager.getInstallSourceInfo(context.getPackageName());
                installingPackageName = installer.getInstallingPackageName();
            }
            else
            {
                installingPackageName = packageManager.getInstallerPackageName(context.getPackageName());
            }
            // A list with valid installers package name
            List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

            // true if your app has been downloaded from Play Store
            return installingPackageName != null && validInstallers.contains(installingPackageName);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    public static boolean isTransactionHash(String input)
    {
        if (input == null || (input.length() != 66 && input.length() != 64)) return false;
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == 64;
    }
}
