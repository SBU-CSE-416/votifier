import React from "react";
import NavBar from "./NavBar";
import MapPg from "./map and data/MapPg";

export default function Votifier() {
  return (
    <section style={{ display: "flex", flexDirection: "column" }}>
      <NavBar></NavBar>
      <div style={{ display: "flex" }}>
        <MapPg></MapPg>
      </div>
    </section>
  );
}
