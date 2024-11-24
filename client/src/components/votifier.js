import { useState } from "react";
import NavBar from "./NavBar";
import HomePg from "./HomePg";
import MapPg from "./map and data/MapPg";

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
    }
  };

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
