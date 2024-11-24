import { useState } from "react";
import NavBar from "./NavBar";
import LeftSideMenu from "./map and data/LeftSideMenu";
import MapPg from "./map and data/MapPg";

export default function Votifier() {
  const [pg, setPage] = useState("home");


  return (
    <section style={{ display: "flex", flexDirection: "column" }}>
      <NavBar setPage={setPage}></NavBar>
      <div style={{ display: "flex" }}>
        <MapPg></MapPg>
        {/* {checkState()} */}
      </div>
    </section>
  );
}
