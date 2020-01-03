package Common.Model;

public enum Genre
{
    AFRO,
    ALTERNATIVE_ROCK,
    AVANT_GARDE,
    BLUES,
    CARIBBEAN,
    CHILDREN_S,
    CHRISTIAN,
    CLASSIC,
    COMEDY,
    CONTEMPORARY,
    CONTEMPORARY_RNB,
    COUNTRY,
    DANCE,
    ELETRONIC,
    FLAMENCO,
    FOLK,
    FUSION,
    HARDCORE,
    HEAVY_METAL,
    HIP_HOP,
    HOUSE,
    INSTRUMENTAL,
    JAZZ,
    LATIN,
    OPERA,
    POLKA,
    POP,
    PSYCHEDELIC,
    PUNK_ROCK,
    REGGAE,
    RNB,
    ROCK,
    SOUL,
    THECNO,
    TRANCE,
    TRAP,
    VOCAL,
    WEDDING,
    UNDEFINED;


    public String toName()
    {
        String text = this.toString();

        if (text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convert_next = true;
        for (char ch : text.toCharArray())
        {
            if(ch == '_')
                ch = ' ';

            if (Character.isSpaceChar(ch))
            {
                convert_next = true;
            }
            else if (convert_next)
            {
                ch = Character.toTitleCase(ch);
                convert_next = false;
            }
            else
            {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }
}
