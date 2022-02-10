package com.example.demo;

import com.example.demo.model.UserRequest;
import com.example.demo.model.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class DemoService {

    @Autowired
    UserRepository userRepository;

    public UserResponse getUser(UserRequest request){
        log.info("start get user service... ");
        log.info("request : {}", request);

        UserResponse user = new UserResponse();

        String uri = new String("https://randomuser.me/api");
        if (request.getSeed() != null) {
            if (!userRepository.findById(request.getSeed()).isEmpty()) {
                user = userRepository.findById(request.getSeed()).get();
                if (user.getSeed() != null) {
                    return user;
                }
            }
            uri += "/?seed=" + request.getSeed();
            log.info("uri : {} ", uri);
        }

        try{
            RestTemplate rest = new RestTemplate();
            ResponseEntity<String> response = rest.exchange(uri, HttpMethod.GET, null, String.class);
            //response
            System.out.println(response.getBody());
            JSONObject json = new JSONObject(response.getBody());
            JSONArray jsonA = json.getJSONArray("results");
            JSONObject jsonInfo = json.getJSONObject("info");

            user.setSeed(jsonInfo.getString("seed"));

            //Detail result
            for (int i = 0; i<jsonA.length();i++) {
                JSONObject jsonResult = jsonA.getJSONObject(i);
                user.setGender(jsonResult.getString("gender"));

                JSONObject jsonName = jsonResult.getJSONObject("name");
                user.setName(
                        jsonName.getString("title") + " " +
                        jsonName.getString("first") + " " +
                        jsonName.getString("last")
                );

                JSONObject jsonAddress = jsonResult.getJSONObject("location");
                JSONObject jsonStreet = jsonAddress.getJSONObject("street");
                user.setAddress(
                        jsonStreet.getInt("number") + " " +
                        jsonStreet.getString("name") + " " +
                        jsonAddress.getString("city") + " " +
                        jsonAddress.getString("state") + " " +
                        jsonAddress.getString("country") + " " +
                        jsonAddress.get("postcode")
                );

                user.setEmail(jsonResult.getString("email"));
            }

            userRepository.save(user);

        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}
