package com.sugestio.client.model.detail;


public class StarRating extends Detail {

        private double maxRating;
        private double minRating;
        private double userRating;


        public StarRating (double maxRating, double minRating, double userRating) {
            this.maxRating = maxRating;
            this.minRating = minRating;
            this.userRating = userRating;
        }

        @Override
        public String toString() {
            return "STAR:" + maxRating + ":" + minRating + ":" + userRating;
        }
    }
