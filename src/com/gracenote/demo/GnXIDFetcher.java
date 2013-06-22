package com.gracenote.demo;

/**
 * @class This class simplifies the interface for retrieving external ID's associated with GN metadata objects. 
 * Note: The fetch methods perform a synchronous network operation and should be called from a background thread
 */

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Iterator;

import com.gracenote.gnsdk.Epg.GnEpg;
import com.gracenote.gnsdk.Manager.GnException;
import com.gracenote.gnsdk.Manager.GnUser;
import com.gracenote.gnsdk.Metadata.GnExternalID;
import com.gracenote.gnsdk.Metadata.GnIterator;
import com.gracenote.gnsdk.Metadata.GnResult;
import com.gracenote.gnsdk.MetadataACR.IGnAcrMatch;
import com.gracenote.gnsdk.MetadataEPG.IGnTvAiring;
import com.gracenote.gnsdk.MetadataEPG.IGnTvChannel;
import com.gracenote.gnsdk.MetadataEPG.IGnTvProgram;
import com.gracenote.gnsdk.MetadataVideo.IGnVideoWork;
import com.gracenote.gnsdk.Video.GnVideo;

public class GnXIDFetcher {

	private GnUser mUser = null;
	
	/**
	 * @brief  Constructor
	 * @param user The global GnUser object initialized with your Entourage client id
	 */
	public GnXIDFetcher(GnUser user) {

		assert(mUser != null);
		mUser = user;
	}
	
	
	/**
	 * @brief Fetch external ID's from a TV Channel
	 * @param channel The channel you want to retrieve external ID's for
	 * @throws GnException
	 * @return An iterator to a list of GnExternal ID's
	 */
	public Iterator<GnExternalID> fetchTvChannelExternalIDs(IGnTvChannel channel) throws GnException
	{
		return this.fetchTvChannelExternalIDs(channel, "");
	}
	
	/**
	 * @brief Fetch external ID's from a TV Channel
	 * @param channel The channel you want to retrieve external ID's for
	 * @param preferredSource The preferred external ID source (e.g. "tmsid"). 
	 * 		  Results will only include external ID's from this source. Can be empty
	 * @throws GnException
	 * @return An iterator to a list of GnExternal ID's
	 */
	public Iterator<GnExternalID> fetchTvChannelExternalIDs(IGnTvChannel channel, String preferredSource) throws GnException
	{
		ArrayList<GnExternalID> list = new ArrayList<GnExternalID>();
		
		GnEpg epg = new GnEpg(mUser);
		epg.setOptionEnableLinkData(true);
		GnResult result = epg.findChannels(channel);
		
		GnIterator tvChannels;
		try {
			tvChannels = result.getTvChannels();
			while (tvChannels.hasNext())
			{
				IGnTvChannel fullChannel = (IGnTvChannel)tvChannels.next();
				GnIterator externalIDs = fullChannel.getExternalIDs();
				while (externalIDs.hasNext()) {
					GnExternalID xid = (GnExternalID) externalIDs.next();
					if (preferredSource.isEmpty() || preferredSource.equalsIgnoreCase(xid.getSource())) {
						list.add(xid);
					}
				}
			}
		} catch (InvalidObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return list.iterator();
		
	}
	
	
	
	
	
	
	
	/**
	 * @brief Fetch external ID's from a TV Program
	 * @param program The channel you want to retrieve external ID's for
	 * @throws GnException
	 * @return An iterator to a list of GnExternal ID's
	 */
	public Iterator<GnExternalID> fetchTvProgramExternalIDs(IGnTvProgram program) throws GnException
	{
		return this.fetchTvProgramExternalIDs(program, "");
	}
	
	
	/**
	 * @brief Fetch external ID's from a TV Program
	 * @param program The program you want to retrieve external ID's for
	 * @param preferredSource The preferred external ID source (e.g. "tmsid"). 
	 * 		  Results will only include external ID's from this source. Can be empty
	 * @throws GnException
	 * @return An iterator to a list of GnExternal ID's
	 */
	public Iterator<GnExternalID> fetchTvProgramExternalIDs(IGnTvProgram program, String preferredSource) throws GnException
	{
		ArrayList<GnExternalID> list = new ArrayList<GnExternalID>();
		GnEpg epg = new GnEpg(mUser);
		epg.setOptionEnableLinkData(true);
		GnResult result = epg.findPrograms(program);
		
		GnIterator tvPrograms;
		try {
			tvPrograms = result.getTvPrograms();
			while (tvPrograms.hasNext())
			{
				IGnTvProgram fullProgram = (IGnTvProgram)tvPrograms.next();
				GnIterator externalIDs = fullProgram.getExternalIDs();
				while (externalIDs.hasNext()) {
					GnExternalID xid = (GnExternalID) externalIDs.next();
					if (preferredSource.isEmpty() || preferredSource.equalsIgnoreCase(xid.getSource())) {
						list.add(xid);
					}
				}
			}
		} catch (InvalidObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list.iterator();
	}
	
	
	/**
	 * @brief Fetch external ID's from a GnVideoWork
	 * @param match The GnAcrMatch object containing the Video Work you want to retrieve external ID's for
	 * @throws GnException
	 * @return An iterator to a list of GnExternal ID's
	 */
	public Iterator<GnExternalID> fetchVideoWorkExternalIDs(IGnAcrMatch match) throws GnException
	{
		return this.fetchVideoWorkExternalIDs(match, "");
	}
	
	
	/**
	 * @brief Fetch external ID's from a Video Work
	 * @param match The GnAcrMatch object containing the Video Work you want to retrieve external ID's for
	 * @param preferredSource The preferred external ID source (e.g. "tmsid"). 
	 * 		  Results will only include external ID's from this source. Can be empty
	 * @throws GnException
	 * @return An iterator to a list of GnExternal ID's
	 */
	public Iterator<GnExternalID> fetchVideoWorkExternalIDs(IGnAcrMatch match, String preferredSource) throws GnException
	{
		ArrayList<GnExternalID> list = new ArrayList<GnExternalID>();
		
		IGnTvAiring airing = match.getTvAiring();
        IGnVideoWork work = match.getAvWork();
         
        try {
            // If an airing exists the Video Work must be accessed through the TvProgram object
        	if (airing != null) {
                
                // Create a new query object
                GnEpg epgQuery = new GnEpg(mUser);
                 
                // Retrieve full program metadata
                IGnTvProgram tvProgram = airing.getTvProgram();
                GnResult epgResult = epgQuery.findPrograms(tvProgram);
                 
                // Iterate over returned TV Programs to get the associated Video Work
                // Very unlikely this will return more than a single program
                GnIterator programs = epgResult.getTvPrograms();
                while (programs.hasNext()) {
                    IGnTvProgram fullProgram = (IGnTvProgram)programs.next();
                    IGnVideoWork theWork = fullProgram.getAvWork();

                    if (theWork != null) {
                        // Create a Video Work query object and enable the link data option
                        // to ensure that external ID's will be returned
                        GnVideo videoQuery = new GnVideo(mUser);
                        videoQuery.setOptionEnableLinkData(true);

                        // Retrieve the full Video Work metadata
                        GnResult vidResult = videoQuery.findWorks(theWork);
                        GnIterator videoWorks = vidResult.getVideoWorks();

                        // Iterate over the full works and retrieve the external ID
                        while (videoWorks.hasNext()) {
                            IGnVideoWork fullWork = (IGnVideoWork)videoWorks.next();
                            GnIterator xids = fullWork.getExternalIDs();
                            while (xids.hasNext()) {
                            	GnExternalID xid = (GnExternalID) xids.next();
                            	if (preferredSource.isEmpty() || preferredSource.equalsIgnoreCase(xid.getSource())) {
            						list.add(xid);
            					}
                            }
                        }   
                    }                                   
                }                       
            }
            // Depending on the match type there could be a Video Work accessible directly from the match object
            else if (work != null) {
                 
                // Create a Video Work query object and enable the link data option
                // to ensure that external ID's will be returned
                GnVideo videoQuery = new GnVideo(mUser);
                videoQuery.setOptionEnableLinkData(true);
                 
                // Retrieve the full Video Work metadata
                GnResult vidResult = videoQuery.findWorks(work);
                GnIterator videoWorks = vidResult.getVideoWorks();
                 
                // Iterate over the full works and retrieve the external ID
                while (videoWorks.hasNext()) {
                    IGnVideoWork fullWork = (IGnVideoWork)videoWorks.next();
                    GnIterator xids = fullWork.getExternalIDs();
                    while (xids.hasNext()) {
                        GnExternalID xid = (GnExternalID)xids.next();
                        if (preferredSource.isEmpty() || preferredSource.equalsIgnoreCase(xid.getSource())) {
    						list.add(xid);
    					}
                    }
                }   
            }
        } catch (InvalidObjectException e) {
        	e.printStackTrace();
        }
        
   

		return list.iterator();
	}

	
	

}