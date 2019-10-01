package com.wideorbit.wostreaming;

import java.util.ArrayList;

class LotameAudienceResponse {
    Profile Profile;

    class Profile {
        String tpid;
        Audiences Audiences;

        class Audiences {
            ArrayList<Audience> Audience = new ArrayList<>();
        }

        class Audience {
            String id;
        }
    }
}