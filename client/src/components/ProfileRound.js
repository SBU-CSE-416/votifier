import "../stylesheets/profileRound.css"
import { VscAccount } from "react-icons/vsc";

export default function ProfileRound() {
    return(
        <div className="card-container">
          <div className="card">
            <div className="roundel-border">
            <img
                src="/kaitlyn.png"
                alt="Kaitlyn Chau"
                style={{
                  width: "250px",  // Same as the size of VscAccount
                  height: "250px",
                  borderRadius: "50%", // Makes the image circular
                  objectFit: "cover", // Ensures the image covers the area without distortion
                  margin: "4px"
                }}
              />
            </div>
            <h1 className="centered-text-h1">Kaitlyn Chau</h1>
          </div>
          <div className="card">
            <div className="roundel-border">
              <img
                src="/tony.png"
                alt="Tony Bui"
                style={{
                  width: "250px",  // Same as the size of VscAccount
                  height: "250px",
                  borderRadius: "50%", // Makes the image circular
                  objectFit: "cover", // Ensures the image covers the area without distortion
                  margin: "4px"
                }}
                />
            </div>
            <h1 className="centered-text-h1">Tony Bui</h1>
          </div>
          <div className="card">
            <div className="roundel-border">
              <img
                src="/vitaliy.png"
                alt="Vitaliy Stusalitus"
                style={{
                  width: "250px",  // Same as the size of VscAccount
                  height: "250px",
                  borderRadius: "50%", // Makes the image circular
                  objectFit: "cover", // Ensures the image covers the area without distortion
                  margin: "4px"
                }}
                />
            </div>
            <h1 className="centered-text-h1">Vitaliy Stusalitus</h1>
          </div>
          <div className="card">
            <div className="roundel-border">
              <VscAccount size={250} style={{"margin":"4px"}}></VscAccount>
              {/* <img
                src="/jack.png"
                alt="Jack Zhang"
                style={{
                  width: "250px",  // Same as the size of VscAccount
                  height: "250px",
                  borderRadius: "50%", // Makes the image circular
                  objectFit: "cover", // Ensures the image covers the area without distortion
                  margin: "4px"
                }}
                /> */}
            </div>
            <h1 className="centered-text-h1">Jack Zhang</h1>
          </div>

        </div>
    );
}