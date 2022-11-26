package com.example.splashscreen;

public class EncriptarTexto
{

    protected static String encriptar(String dato)
    {
        String s = "";
        String matriz[][] =
                {
                        {
                                "ž", "ǁ", "(", "΅", "g", "ʏ", "S", "ɂ", "ʔ", "ɛ", "¤", "˝", "Ʊ", "͝", "o", "˥", "Ķ", "Ɋ", "͞", "ǰ", "ę", "", "Ͳ", "3", "̅", "Ɔ", "×", "͏", "ĕ", "Ⱥ"
                        },
                        {
                                "Ǣ", "̈́", "ͱ", "ˈ", "Ǯ", "Ƙ", "Ⱦ", "Ď", "ȭ", "̾", "ʷ", "Ə", "¡", "˟", "ͷ", "Ó", "Ț", "ͺ", "Ɓ", "Ǝ", "˴", "Ú", "ɗ", "", "E", "˔", "ɨ", "̲", "", "ü"
                        },
                        {
                                "ȅ", "ʩ", "ˌ", "", "˓", "˘", "č", "ʉ", "", "ƛ", "Ȋ", "Ʃ", "û", "ƴ", "Ŗ", "į", "Ǉ", "Į", "ʮ", "¥", "Ɯ", "þ", "˙", "̆", "Ɍ", "ĭ", "ͣ", "ɕ", "", "9"
                        },
                        {
                                "ǅ", "͖", "̇", "Ή", "̣", "z", "Æ", "Ǻ", "ē", "̥", "Ɂ", "Ĉ", "Β", "ɒ", "ǵ", "Q", "Ǖ", "ʤ", "Ε", "±", "Ȥ", "ţ", "ǹ", "ʑ", "ǭ", "f", "_", "ˮ", "̕", "·"
                        },
                        {
                                "R", "ô", "ȥ", "µ", "ã", "Ë", "˿", "", "ó", "̤", "d", "̓", "΄", " ", "ě", "ʙ", "Ͷ", "Γ", "ʊ", "ɀ", "ʁ", "͉", "ʡ", "Ĵ", "÷", "ɴ", "̡", "ì", "ğ", "Ǧ"
                        },
                        {
                                "ɲ", "ŉ", "´", "ʣ", "Ȯ", "c", "Ȭ", "Ι", "̼", "ʘ", "ź", "͐", "", "Ȍ", "Ź", "ȓ", "B", "Ʈ", "", "ć", "ǈ", "ß", "Ǵ", "ł", "ȵ", "Z", "ċ", "ʞ", "", "ɽ"
                        },
                        {
                                "Ū", "p", "Ľ", "ɳ", "Ƒ", "˷", "˗", "ĳ", "˒", "͎", "ñ", "š", "˵", "Ʉ", "̯", "ɭ", "Ƀ", "ŵ", "ʟ", "N", "˚", "Ǭ", "̿", "W", "ŋ", "?", "", "ř", "ŕ", "ʿ"
                        },
                        {
                                "́", "ʳ", "Ȫ", "͟", "̘", "Ɵ", ":", "ʎ", "Ɖ", "Ñ", "Ō", "", "U", "Ƌ", "ͩ", "ƶ", ">", "ȸ", "ȿ", "΁", "T", "͜", "ͨ", "Ç", "", "̋", "Ƴ", "ǂ", "͠", ""
                        },
                        {
                                ";", "Þ", "Ę", "ș", "Ŋ", "£", "ǜ", "˲", "=", "ľ", "Μ", "Ĕ", "Ƈ", "ʫ", "Ʋ", "ˆ", "Ƅ", "ƽ", "˱", "Ș", "Ƭ", "Ȧ", "Ħ", "Ȃ", "h", "\\", "î", "ǩ", "ı", "ƾ"
                        },
                        {
                                "Ȱ", "ͪ", "̹", "ʜ", "#", "<", "Ȇ", "͆", "e", ".", "ƻ", "̧", "É", "̵", "2", "ʭ", "̳", "ŗ", "/", "ʇ", "̫", "Ů", "Ͱ", "ʯ", "A", "̚", "ĩ", "8", "ŭ", "˧"
                        },
                        {
                                "˯", "`", "\"", "K", "ɶ", "À", "̢", "ͅ", "ˑ", "ɸ", "è", "ŏ", "¼", "ǆ", "Ά", "Ώ", "ɰ", "¦", "ɞ", "", "̖", "΋", "Ƽ", "Ǚ", "Ģ", "»", "Ȼ", "ɟ", "Ȓ", "ˣ"
                        },
                        {
                                "ɖ", "˂", "ɘ", "Ư", "Ƃ", "̀", "ͥ", "ʃ", "ɥ", "ƚ", "ʓ", "Ǹ", "˸", "4", ",", "Ȁ", "˪", "Ð", "̌", "ǖ", "ĺ", "ȧ", "Û", "J", "̒", "ɏ", "|", "", "ƫ", "Ǔ"
                        },
                        {
                                "'", "ƥ", "Ȏ", "ż", "ʌ", "˽", "͔", "Ɇ", "Ȅ", "", "ɔ", "ƃ", "ģ", "Ξ", "Ő", "ɉ", "¹", "ʼ", "ɢ", "+", "ˀ", "Ȣ", "ʐ", "w", "Ƨ", "̃", "͕", "ǫ", "˦", ""
                        },
                        {
                                "á", "̑", "Ν", "ƍ", "n", "ɝ", "ş", "ȏ", "ɫ", "̞", "ą", "ȣ", "&", "˄", "΂", "ī", "ų", "͘", "Ø", "ŝ", "Ǡ", "ò", "Ȝ", "˛", "Î", "ǐ", "ņ", "ˤ", "I", "ʵ"
                        },
                        {
                                "ǳ", "ȝ", "Ǘ", "ʒ", "Ž", "ɼ", "Ŷ", "ď", "ǘ", "Ɉ", "!", "͑", "r", "˹", "Ɠ", "˶", "u", "ɹ", "Ł", "ǲ", "D", "ŀ", "ù", "ǀ", "ÿ", "ú", "Σ", "G", "ǔ", "͓"
                        },
                        {
                                "ǧ", "Ɩ", "Ǥ", "ä", "Ȟ", "˾", "ȳ", "Ŝ", "ƨ", "͍", "Ƞ", "ð", "Š", "ˁ", "Ų", "ʹ", "ũ", "í", "§", "Ƣ", "F", "Ǩ", "0", "̨", "Ί", "·", "ê", "q", "Ě", "˖"
                        },
                        {
                                "̙", "Ǽ", "ț", "Λ", "̶", "Č", "ͯ", "ȴ", "̷", "ů", "ˎ", "Ƿ", "ȁ", "ǃ", "l", "¸", "ɵ", "Y", "ɐ", "%", "ɡ", "ʰ", "͂", "Ű", "̉", "Ï", "İ", "ķ", "Ƚ", "Å"
                        },
                        {
                                "ˏ", "Ã", "ͽ", "ɑ", "̺", ")", "ʽ", "Ŵ", "v", "Ŀ", "", "Ü", "ǌ", "ȩ", "", "˜", "1", "˃", "ͼ", "Đ", "x", "Ş", "Ǿ", "ˡ", "œ", "̎", "*", "ª", "³", "͢"
                        },
                        {
                                "", "ƈ", "ơ", "Ɣ", "˅", "ɋ", "̛", "¾", "̏", "ĵ", "ɻ", "ʕ", "ʥ", "Ȉ", "H", "-", "ͳ", "ǝ", "ĸ", "̽", "ś", "ʻ", "Ń", "΃", "¢", "$", "Ǜ", "ʚ", "ǥ", "j"
                        },
                        {
                                "͒", "k", "ǟ", "ȕ", "Α", "ʴ", "", "ť", "̗", "ű", "Ğ", "̟", "̓", "̜", "ʹ", "͌", "ǒ", "ǣ", "Ŭ", "ɓ", "Ĩ", "Ƥ", "Ĭ", "Δ", "Œ", "«", "ȫ", "Ŏ", "Ê", "^"
                        },
                        {
                                "ɦ", "Έ", "ː", "ɱ", "Ņ", "ƿ", "", "Ý", "˕", "Ƶ", "ĥ", "", "ȇ", "ă", "ŧ", "ʋ", "̈", "Ȩ", "", "˰", "̩", "ʺ", "Ţ", "˻", "ƞ", "͙", "ƹ", "ƌ", "ø", "Ļ"
                        },
                        {
                                "̻", "ɧ", "ʢ", "â", "͵", "Ÿ", "ˊ", "Ʀ", "L", "ͻ", "°", "¨", "­", "ĉ", "ő", "®", "7", "M", "ý", "a", "Ż", "", "ư", "ɤ", "ƭ", "ǚ", "΍", "ʾ", "Ρ", "ǽ"
                        },
                        {
                                "P", "ɠ", "ʂ", "Ο", "}", "ͮ", "ĝ", "È", "˳", "y", "Ĺ", "͗", "ħ", "͊", "½", "ͭ", "ç", "ʀ", "õ", "̬", "˫", "¿", "Ť", "Ǎ", "", "̝", "ɍ", "ɬ", "V", "̱"
                        },
                        {
                                "˺", "ʛ", "ʸ", "ʱ", "̍", "ɾ", "Ʌ", "΢", "Ϳ", "ǻ", "Ǌ", "ŷ", "ͦ", "ˉ", "ň", "ʠ", "Ǒ", "ȱ", "]", "ȗ", "Ύ", "ͬ", "O", "ɮ", "ë", "{", "Ζ", "ȋ", "ǎ", "Ũ"
                        },
                        {
                                "Í", "ˢ", "ń", "Θ", "", "Ɗ", "[", "ʝ", "ȷ", "Ā", "©", "Ĳ", "ɚ", "ʶ", "ȍ", "b", "̴", "ƙ", "ͤ", "̭", "ƺ", "˭", "Ä", "¯", "̂", "Ȗ", "s", "ȉ", "ſ", "͛"
                        },
                        {
                                "Ś", "ʲ", "Ă", "ġ", "ǉ", "Ô", "ɜ", "ȶ", "Ȑ", "͹", "ʖ", "ȃ", "ö", "Ɲ", "̰", "ǋ", "Ȳ", "æ", "̊", "Ö", "ɣ", "¶", "ȟ", "Ċ", "Ù", "¬", "", "Ɨ", "˞", "t"
                        },
                        {
                                "X", "ˇ", "Ġ", "Η", "Ī", "å", "Â", "Ć", "ʦ", "ɷ", "5", "Ò", "ȼ", "ˍ", "C", "ə", "̦", "ï", "º", "m", "ǯ", "ū", "ƕ", "͡", "ė", "Ǆ", "ƒ", "̠", "", "ƅ"
                        },
                        {
                                "Ƕ", "ȹ", "ʅ", "ō", "ʆ", "ʨ", "΀", "ȑ", "Ǫ", "ɪ", "ƪ", "Ì", "˩", "Ơ", "Ř", "ȡ", "Ɏ", "˼", "ˠ", "ɇ", "ˬ", "", "Ǐ", " ", "đ", "̮", "²", "ļ", "à", "Ɛ"
                        },
                        {
                                "Ǳ", "ȯ", "ʍ", ";", "̔", "Ʒ", "͋", "Ŧ", "Ė", "Õ", "é", "ΐ", "Ŕ", "͈", "Á", "Κ", "ˋ", "ʬ", "Ĥ", "", "ͫ", "ɺ", "Ƹ", "̐", "ǡ", "ɿ", "ʪ", "Ą", "͚", "ʄ"
                        },
                        {
                                "̄", "i", "Ό", "͸", "Ĝ", "6", "ǿ", "Ǟ", "ā", "ʗ", "Ň", "ͧ", "ɩ", "~", "̪", "ƀ", "Ē", "˨", "͇", "Π", "ƣ", "Ȕ", "ʈ", "", "ʧ", "́", "̸", "@", "ɯ", "̀"
                        }
                };
        String coordenadas = "";
        int j;
        int k;
        if (dato != null)
        {
            for (int i = 0; i < dato.length(); i++)
            {
                for (j = 0; j < matriz.length; j++)
                {
                    for (k = 0; k < matriz.length; k++)
                    {
                        if (matriz[j][k].equals(dato.substring(i, i + 1)))
                        {
                            if (j >= 10)
                            {
                                String letra = (char) (55 + j) + "";
                                coordenadas += letra;
                            } else
                            {
                                coordenadas += j;
                            }
                            if (k >= 10)
                            {
                                String letra = (char) (55 + k) + "";
                                coordenadas += letra;
                            } else
                            {
                                coordenadas += k;
                            }
                            k = matriz[j].length;
                            j = matriz.length;
                        }
                    }
                }
            }
            String mitad1 = coordenadas.substring(0, coordenadas.length() / 2);
            String mitad2 = coordenadas.substring(coordenadas.length() / 2, coordenadas.length());
            for (int i = 0; i < mitad1.length(); i++)
            {
                String coordenada = mitad1.substring(i, i + 1) + mitad2.substring(i, i + 1);
                int f;
                try
                {
                    f = Integer.parseInt(coordenada.substring(0, 1));
                } catch (Exception e)
                {
                    char c[] = coordenada.substring(0, 1).toCharArray();
                    f = Integer.parseInt((int) (c[0] - 55) + "");
                }
                int c;
                try
                {
                    c = Integer.parseInt(coordenada.substring(1, 2));
                } catch (Exception e)
                {
                    char c1[] = coordenada.substring(1, 2).toCharArray();
                    c = Integer.parseInt((int) (c1[0] - 55) + "");
                }
                s += matriz[f][c];
            }

        }
        return s;
    }

    protected static String desencriptar(String dato)
    {
        String s = "";

        String matriz[][] =
                {
                        {
                                "ž", "ǁ", "(", "΅", "g", "ʏ", "S", "ɂ", "ʔ", "ɛ", "¤", "˝", "Ʊ", "͝", "o", "˥", "Ķ", "Ɋ", "͞", "ǰ", "ę", "", "Ͳ", "3", "̅", "Ɔ", "×", "͏", "ĕ", "Ⱥ"
                        },
                        {
                                "Ǣ", "̈́", "ͱ", "ˈ", "Ǯ", "Ƙ", "Ⱦ", "Ď", "ȭ", "̾", "ʷ", "Ə", "¡", "˟", "ͷ", "Ó", "Ț", "ͺ", "Ɓ", "Ǝ", "˴", "Ú", "ɗ", "", "E", "˔", "ɨ", "̲", "", "ü"
                        },
                        {
                                "ȅ", "ʩ", "ˌ", "", "˓", "˘", "č", "ʉ", "", "ƛ", "Ȋ", "Ʃ", "û", "ƴ", "Ŗ", "į", "Ǉ", "Į", "ʮ", "¥", "Ɯ", "þ", "˙", "̆", "Ɍ", "ĭ", "ͣ", "ɕ", "", "9"
                        },
                        {
                                "ǅ", "͖", "̇", "Ή", "̣", "z", "Æ", "Ǻ", "ē", "̥", "Ɂ", "Ĉ", "Β", "ɒ", "ǵ", "Q", "Ǖ", "ʤ", "Ε", "±", "Ȥ", "ţ", "ǹ", "ʑ", "ǭ", "f", "_", "ˮ", "̕", "·"
                        },
                        {
                                "R", "ô", "ȥ", "µ", "ã", "Ë", "˿", "", "ó", "̤", "d", "̓", "΄", " ", "ě", "ʙ", "Ͷ", "Γ", "ʊ", "ɀ", "ʁ", "͉", "ʡ", "Ĵ", "÷", "ɴ", "̡", "ì", "ğ", "Ǧ"
                        },
                        {
                                "ɲ", "ŉ", "´", "ʣ", "Ȯ", "c", "Ȭ", "Ι", "̼", "ʘ", "ź", "͐", "", "Ȍ", "Ź", "ȓ", "B", "Ʈ", "", "ć", "ǈ", "ß", "Ǵ", "ł", "ȵ", "Z", "ċ", "ʞ", "", "ɽ"
                        },
                        {
                                "Ū", "p", "Ľ", "ɳ", "Ƒ", "˷", "˗", "ĳ", "˒", "͎", "ñ", "š", "˵", "Ʉ", "̯", "ɭ", "Ƀ", "ŵ", "ʟ", "N", "˚", "Ǭ", "̿", "W", "ŋ", "?", "", "ř", "ŕ", "ʿ"
                        },
                        {
                                "́", "ʳ", "Ȫ", "͟", "̘", "Ɵ", ":", "ʎ", "Ɖ", "Ñ", "Ō", "", "U", "Ƌ", "ͩ", "ƶ", ">", "ȸ", "ȿ", "΁", "T", "͜", "ͨ", "Ç", "", "̋", "Ƴ", "ǂ", "͠", ""
                        },
                        {
                                ";", "Þ", "Ę", "ș", "Ŋ", "£", "ǜ", "˲", "=", "ľ", "Μ", "Ĕ", "Ƈ", "ʫ", "Ʋ", "ˆ", "Ƅ", "ƽ", "˱", "Ș", "Ƭ", "Ȧ", "Ħ", "Ȃ", "h", "\\", "î", "ǩ", "ı", "ƾ"
                        },
                        {
                                "Ȱ", "ͪ", "̹", "ʜ", "#", "<", "Ȇ", "͆", "e", ".", "ƻ", "̧", "É", "̵", "2", "ʭ", "̳", "ŗ", "/", "ʇ", "̫", "Ů", "Ͱ", "ʯ", "A", "̚", "ĩ", "8", "ŭ", "˧"
                        },
                        {
                                "˯", "`", "\"", "K", "ɶ", "À", "̢", "ͅ", "ˑ", "ɸ", "è", "ŏ", "¼", "ǆ", "Ά", "Ώ", "ɰ", "¦", "ɞ", "", "̖", "΋", "Ƽ", "Ǚ", "Ģ", "»", "Ȼ", "ɟ", "Ȓ", "ˣ"
                        },
                        {
                                "ɖ", "˂", "ɘ", "Ư", "Ƃ", "̀", "ͥ", "ʃ", "ɥ", "ƚ", "ʓ", "Ǹ", "˸", "4", ",", "Ȁ", "˪", "Ð", "̌", "ǖ", "ĺ", "ȧ", "Û", "J", "̒", "ɏ", "|", "", "ƫ", "Ǔ"
                        },
                        {
                                "'", "ƥ", "Ȏ", "ż", "ʌ", "˽", "͔", "Ɇ", "Ȅ", "", "ɔ", "ƃ", "ģ", "Ξ", "Ő", "ɉ", "¹", "ʼ", "ɢ", "+", "ˀ", "Ȣ", "ʐ", "w", "Ƨ", "̃", "͕", "ǫ", "˦", ""
                        },
                        {
                                "á", "̑", "Ν", "ƍ", "n", "ɝ", "ş", "ȏ", "ɫ", "̞", "ą", "ȣ", "&", "˄", "΂", "ī", "ų", "͘", "Ø", "ŝ", "Ǡ", "ò", "Ȝ", "˛", "Î", "ǐ", "ņ", "ˤ", "I", "ʵ"
                        },
                        {
                                "ǳ", "ȝ", "Ǘ", "ʒ", "Ž", "ɼ", "Ŷ", "ď", "ǘ", "Ɉ", "!", "͑", "r", "˹", "Ɠ", "˶", "u", "ɹ", "Ł", "ǲ", "D", "ŀ", "ù", "ǀ", "ÿ", "ú", "Σ", "G", "ǔ", "͓"
                        },
                        {
                                "ǧ", "Ɩ", "Ǥ", "ä", "Ȟ", "˾", "ȳ", "Ŝ", "ƨ", "͍", "Ƞ", "ð", "Š", "ˁ", "Ų", "ʹ", "ũ", "í", "§", "Ƣ", "F", "Ǩ", "0", "̨", "Ί", "·", "ê", "q", "Ě", "˖"
                        },
                        {
                                "̙", "Ǽ", "ț", "Λ", "̶", "Č", "ͯ", "ȴ", "̷", "ů", "ˎ", "Ƿ", "ȁ", "ǃ", "l", "¸", "ɵ", "Y", "ɐ", "%", "ɡ", "ʰ", "͂", "Ű", "̉", "Ï", "İ", "ķ", "Ƚ", "Å"
                        },
                        {
                                "ˏ", "Ã", "ͽ", "ɑ", "̺", ")", "ʽ", "Ŵ", "v", "Ŀ", "", "Ü", "ǌ", "ȩ", "", "˜", "1", "˃", "ͼ", "Đ", "x", "Ş", "Ǿ", "ˡ", "œ", "̎", "*", "ª", "³", "͢"
                        },
                        {
                                "", "ƈ", "ơ", "Ɣ", "˅", "ɋ", "̛", "¾", "̏", "ĵ", "ɻ", "ʕ", "ʥ", "Ȉ", "H", "-", "ͳ", "ǝ", "ĸ", "̽", "ś", "ʻ", "Ń", "΃", "¢", "$", "Ǜ", "ʚ", "ǥ", "j"
                        },
                        {
                                "͒", "k", "ǟ", "ȕ", "Α", "ʴ", "", "ť", "̗", "ű", "Ğ", "̟", "̓", "̜", "ʹ", "͌", "ǒ", "ǣ", "Ŭ", "ɓ", "Ĩ", "Ƥ", "Ĭ", "Δ", "Œ", "«", "ȫ", "Ŏ", "Ê", "^"
                        },
                        {
                                "ɦ", "Έ", "ː", "ɱ", "Ņ", "ƿ", "", "Ý", "˕", "Ƶ", "ĥ", "", "ȇ", "ă", "ŧ", "ʋ", "̈", "Ȩ", "", "˰", "̩", "ʺ", "Ţ", "˻", "ƞ", "͙", "ƹ", "ƌ", "ø", "Ļ"
                        },
                        {
                                "̻", "ɧ", "ʢ", "â", "͵", "Ÿ", "ˊ", "Ʀ", "L", "ͻ", "°", "¨", "­", "ĉ", "ő", "®", "7", "M", "ý", "a", "Ż", "", "ư", "ɤ", "ƭ", "ǚ", "΍", "ʾ", "Ρ", "ǽ"
                        },
                        {
                                "P", "ɠ", "ʂ", "Ο", "}", "ͮ", "ĝ", "È", "˳", "y", "Ĺ", "͗", "ħ", "͊", "½", "ͭ", "ç", "ʀ", "õ", "̬", "˫", "¿", "Ť", "Ǎ", "", "̝", "ɍ", "ɬ", "V", "̱"
                        },
                        {
                                "˺", "ʛ", "ʸ", "ʱ", "̍", "ɾ", "Ʌ", "΢", "Ϳ", "ǻ", "Ǌ", "ŷ", "ͦ", "ˉ", "ň", "ʠ", "Ǒ", "ȱ", "]", "ȗ", "Ύ", "ͬ", "O", "ɮ", "ë", "{", "Ζ", "ȋ", "ǎ", "Ũ"
                        },
                        {
                                "Í", "ˢ", "ń", "Θ", "", "Ɗ", "[", "ʝ", "ȷ", "Ā", "©", "Ĳ", "ɚ", "ʶ", "ȍ", "b", "̴", "ƙ", "ͤ", "̭", "ƺ", "˭", "Ä", "¯", "̂", "Ȗ", "s", "ȉ", "ſ", "͛"
                        },
                        {
                                "Ś", "ʲ", "Ă", "ġ", "ǉ", "Ô", "ɜ", "ȶ", "Ȑ", "͹", "ʖ", "ȃ", "ö", "Ɲ", "̰", "ǋ", "Ȳ", "æ", "̊", "Ö", "ɣ", "¶", "ȟ", "Ċ", "Ù", "¬", "", "Ɨ", "˞", "t"
                        },
                        {
                                "X", "ˇ", "Ġ", "Η", "Ī", "å", "Â", "Ć", "ʦ", "ɷ", "5", "Ò", "ȼ", "ˍ", "C", "ə", "̦", "ï", "º", "m", "ǯ", "ū", "ƕ", "͡", "ė", "Ǆ", "ƒ", "̠", "", "ƅ"
                        },
                        {
                                "Ƕ", "ȹ", "ʅ", "ō", "ʆ", "ʨ", "΀", "ȑ", "Ǫ", "ɪ", "ƪ", "Ì", "˩", "Ơ", "Ř", "ȡ", "Ɏ", "˼", "ˠ", "ɇ", "ˬ", "", "Ǐ", " ", "đ", "̮", "²", "ļ", "à", "Ɛ"
                        },
                        {
                                "Ǳ", "ȯ", "ʍ", ";", "̔", "Ʒ", "͋", "Ŧ", "Ė", "Õ", "é", "ΐ", "Ŕ", "͈", "Á", "Κ", "ˋ", "ʬ", "Ĥ", "", "ͫ", "ɺ", "Ƹ", "̐", "ǡ", "ɿ", "ʪ", "Ą", "͚", "ʄ"
                        },
                        {
                                "̄", "i", "Ό", "͸", "Ĝ", "6", "ǿ", "Ǟ", "ā", "ʗ", "Ň", "ͧ", "ɩ", "~", "̪", "ƀ", "Ē", "˨", "͇", "Π", "ƣ", "Ȕ", "ʈ", "", "ʧ", "́", "̸", "@", "ɯ", "̀"
                        }
                };
        String coordenadas = "";
        int j;
        int k;
        for (int i = 0; i < dato.length(); i++)
        {
            for (j = 0; j < matriz.length; j++)
            {
                for (k = 0; k < matriz.length; k++)
                {
                    if (matriz[j][k].equals(dato.substring(i, i + 1)))
                    {
                        if (j >= 10)
                        {
                            String letra = (char) (55 + j) + "";
                            coordenadas += letra;
                        } else
                        {
                            coordenadas += j;
                        }
                        if (k >= 10)
                        {
                            String letra = (char) (55 + k) + "";
                            coordenadas += letra;
                        } else
                        {
                            coordenadas += k;
                        }
                        k = matriz[j].length;
                        j = matriz.length;
                    }
                }
            }
        }
        String mitad1 = "";
        String mitad2 = "";
        for (int i = 0; i < coordenadas.length(); i++)
        {
            if (i % 2 == 0)
            {
                mitad1 += coordenadas.substring(i, i + 1);
            } else
            {
                mitad2 += coordenadas.substring(i, i + 1);
            }
        }
        coordenadas = mitad1 + mitad2;
        for (int i = 0; i < coordenadas.length(); i += 2)
        {
            String coordenada = coordenadas.substring(i, i + 2);
            int f;
            try
            {
                f = Integer.parseInt(coordenada.substring(0, 1));
            } catch (Exception e)
            {
                char c[] = coordenada.substring(0, 1).toCharArray();
                f = Integer.parseInt((int) (c[0] - 55) + "");
            }
            int c;
            try
            {
                c = Integer.parseInt(coordenada.substring(1, 2));
            } catch (Exception e)
            {
                char c1[] = coordenada.substring(1, 2).toCharArray();
                c = Integer.parseInt((int) (c1[0] - 55) + "");
            }
            s += matriz[f][c];
        }
        return s;
    }


}
