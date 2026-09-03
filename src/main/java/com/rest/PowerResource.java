package com.rest;



import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import backend.Power;
import backend.PowerDao;



@Path("/powers")
public class PowerResource {
	
	@GET
	@Produces( {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON} )
	public List<Power> getPowers(){
		return PowerDao.instance.getPowers();
	}
	
	@GET
	@Produces( {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON} )
	@Path("{powerId}")
	public Power getPower(@PathParam("powerId") String id){
		return PowerDao.instance.getPower(Integer.parseInt(id));
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addPower(
			@FormParam("name") String name, 
			@FormParam("squad") String squad, 
			@FormParam("level") int level,
			@FormParam("position") String position,
			@FormParam("age") int age,
			@FormParam("gender") String gender,
			@Context HttpServletResponse servletResponse) throws IOException {
		Power newPower = new Power();
		newPower.setName(name);
		newPower.setSquad(squad);
		newPower.setLevel(level);
		newPower.setPosition(position);
		newPower.setAge(age);
		newPower.setGender(gender);
		PowerDao.instance.addPower(newPower);
		servletResponse.sendRedirect("../createPower.html");
	
	}
	@PUT
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("{powerId}")
	public void putPower(
			@PathParam("powerId") int id,
			
			@FormParam("name") String name, 
			@FormParam("squad") String squad, 
			@FormParam("level") int level,
			@FormParam("position") String position,
			@FormParam("age") int age,
			@FormParam("gender") String gender,
			@Context HttpServletResponse servletResponse) throws IOException {
		Power newPower = new Power();
		newPower.setName(name);
		newPower.setSquad(squad);
		newPower.setLevel(level);
		newPower.setPosition(position);
		newPower.setAge(age);
		newPower.setGender(gender);
		PowerDao.instance.updatePower(newPower);
		servletResponse.sendRedirect("../createPower.html");
	}
	@DELETE
	@Path("{powerId}")
	public Power removePower(@PathParam("powerId") String id) {
		return PowerDao.instance.deletePower(Integer.parseInt(id));
	}
	
}