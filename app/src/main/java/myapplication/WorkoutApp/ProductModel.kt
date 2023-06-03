package myapplication.WorkoutApp


    class ProductModel (
        private var id: Int,
        private var name: String,
        private var image: Int,
        private var price: String,

        ) :java.io.Serializable  {

        fun getId(): Int {
            return id
        }


        fun getName(): String {
            return name
        }


        fun getImage(): Int {
            return image
        }

        fun getPrice(): String {
            return price
        }


    }
