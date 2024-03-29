package data.missions.sun_ice_testbed;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MissionDefinition implements MissionDefinitionPlugin {

	@Override
	public void defineMission(MissionDefinitionAPI api) {
		api.initFleet(FleetSide.PLAYER, "ICS", FleetGoal.ATTACK, false, 5);
		api.initFleet(FleetSide.ENEMY, "SIM", FleetGoal.ATTACK, true, 5);
		api.setFleetTagline(FleetSide.PLAYER, "TEST");
		api.setFleetTagline(FleetSide.ENEMY, "TEST");

		List<String> variants = new ArrayList<>();
		for (String id : Global.getSettings().getAllVariantIds()) {
			ShipVariantAPI variant = Global.getSettings().getVariant(id);
			if (variant.getHullSpec().getHullId().startsWith("sun_ice_") && variant.getHullSpec().getHullSize() != ShipAPI.HullSize.FIGHTER && !variant.getHullSpec().getHullId().contains("module") && !id.startsWith("mission_")) {
				variants.add(id);
			}
		}
		Collections.sort(variants);

		boolean first = true;
		for (String variant : variants) {
			api.addToFleet(FleetSide.PLAYER, variant, FleetMemberType.SHIP, first);
			first = false;
		}

		api.addToFleet(FleetSide.ENEMY, "remnant_station2_Standard", FleetMemberType.SHIP, false);

		float width = 10000f;
		float height = 10000f;
		api.initMap(-width * 0.5f, width * 0.5f, -height * 0.5f, height * 0.5f);

		float minX = -width * 0.5f;
		float minY = -height * 0.5f;

		api.addNebula(minX + width * 0.66f, minY + height * 0.5f, 2000f);
		api.addNebula(minX + width * 0.25f, minY + height * 0.6f, 1000f);
		api.addNebula(minX + width * 0.25f, minY + height * 0.4f, 1000f);

		for (int i = 0; i < 5; i++) {
			float x = (float) Math.random() * width - width * 0.5f;
			float y = (float) Math.random() * height - height * 0.5f;
			float radius = 100f + (float) Math.random() * 400f;
			api.addNebula(x, y, radius);
		}

		api.addObjective(minX + width * 0.25f + 2000f, minY + height * 0.5f, "sensor_array");
		api.addObjective(minX + width * 0.75f - 2000f, minY + height * 0.5f, "comm_relay");
		api.addObjective(minX + width * 0.33f + 2000f, minY + height * 0.4f, "nav_buoy");
		api.addObjective(minX + width * 0.66f - 2000f, minY + height * 0.6f, "nav_buoy");

		api.addAsteroidField(-(minY + height), minY + height, -45f, 2000f, 20f, 70f, 100);
	}
}