Feature: n11.com - Finartz Test Automation Project

  Scenario: n11.com test

    Given I open n11 page
    
    Then I see title is "n11.com - Alışverişin Uğurlu Adresi"

    And I click sign in button

    Then I see login container equals to "Üye Girişi"

    And I see login form equals to "E-Posta Adresi"

    And I see login button equals to "Üye Girişi"

    And I see facebook button equals to "Facebook ile Giriş"

    When I fill the email
    | email | yusufsar60@gmail.com |

    And I fill the password
    | password |  |

    When I click member login button

    And I see my account equals to "Hesabım"

    And I see my name equals to "Yusuf Sar"

    Then I fill the search field
    | search field | samsung |

    And I click search button

    Then I see result contains text
    |Samsung|
    |için|
    |sonuç bulundu.|

    When I wait for page

    Then I click second page button

    When I wait for page

    Then I see title is "Samsung - n11.com - 2"

    And I see page number equals to "2"

    Then I save the product name and firm name in the background

    When I wait for page

    Then I click add to favorites button

    When I wait for page

    Then I click my account button

    And I see order equals to "Mevcut Sipariş"

    And I see cancel equals to "İptal/Değişim/İade"

    And I see coupons equals to "Kuponlarım"

    And I see garage equals to "Garajım"

    And I see points equals to "Puanlarım"

    And I see wish list equals to "İstek Listelerim"

    And I see comments equals to "Yorumlarım"

    And I see questions equals to "Ürün Sorularım"

    And I see member info equals to "Üyelik Bilgilerim"

    And I see address equals to "Adreslerim"

    And I see change password equals to "Şifre Değiştir"

    And I see member cancel equals to "Üyelik İptali"

    And I see my orders equals to "Siparişlerim"

    And I see my tickets equals to "Biletlerim"

    And I see name and surname equals to "Yusuf Sar"

    Then I click wish list button

    When I click my favorites button
    
    And I see title is "Favorilerim - n11.com"

    And I see subtitle of the favorites equals to "Favorilerim"

    Then I compare the product name in favorites and firm name in favorites is same

    Then I click delete button

    When I wait for page

    And I click okey button
















