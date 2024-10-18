import { useState } from "react";
import NavBar from "./NavBar";
import LeftSideMenu from "./map and data/LeftSideMenu";
import HomePg from "./HomePg";
import MapPg from "./map and data/MapPg";
import CreditPg from "./CreditsPg";

export default function Votifier() {
  const [pg, setPage] = useState("home");

  const checkState = () => {
    if (pg === "home") {
      return (
        <>
          <HomePg></HomePg>
        </>
      );
    } else if (pg === "map") {
      return (
        <>
          <MapPg></MapPg>
        </>
      );
    } else if (pg === "credits") {
      return (
        <>
          <CreditPg></CreditPg>
        </>
      );
    }
  };

  return (
    <section style={{ display: "flex", flexDirection: "column" }}>
      <NavBar setPage={setPage}></NavBar>
      <div style={{ display: "flex" }}>
        {/* {pg !== "home" && pg !== "credits" ? (<LeftSideMenu></LeftSideMenu>) : null} */}
        {checkState()}
      </div>
    </section>
  );
}
