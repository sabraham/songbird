(ns songbird.parsers.twitter.tweet-test
  (:require [songbird.parsers.twitter.tweet :as t])
  (:use [clojure.test]))

(def json-tweet
  "{\"created_at\":\"Tue Feb 21 21:29:07 +0000 2012\",\"id\":172070369035956224,\"id_str\":\"172070369035956224\",\"text\":\"The \\\"http:\\/\\/\\\" at the beginning of URLs is a command to the browser. It stands for \\\"head to this place:\\\" followed by two laser-gun noises.\",\"source\":\"web\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":63846421,\"id_str\":\"63846421\",\"name\":\"Brian Sutorius\",\"screen_name\":\"bsuto\",\"location\":\"\",\"url\":\"http:\\/\\/bsuto.com\",\"description\":\"Every day I wake up and put on my gym shorts one leg at a time.\",\"protected\":false,\"followers_count\":2653,\"friends_count\":70,\"listed_count\":98,\"created_at\":\"Fri Aug 07 22:49:15 +0000 2009\",\"favourites_count\":487,\"utc_offset\":-18000,\"time_zone\":\"Eastern Time (US & Canada)\",\"geo_enabled\":false,\"verified\":false,\"statuses_count\":726,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"FFFFFF\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_background_images\\/315830622\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_background_images\\/315830622\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/2596248603\\/c7mhg7vxl601vcst6jap_normal.jpeg\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/2596248603\\/c7mhg7vxl601vcst6jap_normal.jpeg\",\"profile_banner_url\":\"https:\\/\\/si0.twimg.com\\/profile_banners\\/63846421\\/1358799740\",\"profile_link_color\":\"004080\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"FFFFFF\",\"profile_text_color\":\"222222\",\"profile_use_background_image\":false,\"default_profile\":false,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":2413,\"favorite_count\":733,\"entities\":{\"hashtags\":[],\"urls\":[],\"user_mentions\":[]},\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"}")

(deftest payload->json-test
  (is (= {"text" "this is my status"
          "user" {"screen_name" "sabraham"}}
       (t/payload->json
          "{\"text\": \"this is my status\",\"user\": {\"screen_name\": \"sabraham\"}}"))))

(deftest json->minimal-map-test
  (is (= {"text" "this is my status" "screen_name" "sabraham"}
       (t/json->minimal-map {"text" "this is my status"
                             "user" {"screen_name" "sabraham"}}
                            [["text"] ["user" "screen_name"]]))))

(deftest payload->keyworded-minimal-map-test
  (is (= {:text "this is my status" :screen_name "sabraham"}
         (t/payload->keyworded-minimal-map
          "{\"text\": \"this is my status\",\"user\": {\"screen_name\": \"sabraham\"}}"
          [["text"] ["user" "screen_name"]]))))