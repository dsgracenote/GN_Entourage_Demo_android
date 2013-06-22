package com.gracenote.demo;

import com.gracenote.gnsdk.Link.*;
import com.gracenote.gnsdk.Manager.GnException;
import com.gracenote.gnsdk.Manager.GnUser;
import com.gracenote.gnsdk.Metadata.GnMetadataObject;


/**
@brief This class is intended to provide a simple interface for accessing GnMetadataObject images.

@description This class fetches and stores image for a GnMetadata object.  The initWithObject method fetches the image.  Because the image fetch is synchronous you should create and initialize these objects on a background thread

@see GnMetadataObject, GnLink
*/


public class GnMetadataImage {

	// Private instance variables
	private byte[] mImage = null;
	
	/**
    	@brief          Constructor
    	@description    The constructor will fetch and store the image.  The image fetched will be the first image available of the sizes given in the preferredSizes array.
    					Because the image fetch is a synchronous network operation these objects should be created on a background thread.
 
    	@param object The GnMetadataObject you want to retrieve an image for.  Supported objects are GnVideoWork, GnTvProgram, GnContributor, and GnTvChannel
 
    	@param user The global GnUser object for the application instance
 
    	@param preferredSizes An array of preferred image sizes as defined in GnLink.h.  The image retrieved will be the first occurence of an image matching the size in the imageSizes array.  You can prioritize size(s) by sorting them to the top of the array.
	 * 	@throws GnException 
	 */
	
	public GnMetadataImage(GnMetadataObject object, 
							GnUser user, 
							GnLinkTypes.GnCoverArtImageType[] preferredSizes) throws GnException {
		
			GnLink link = new GnLink(user, object);
			if (link.getImageItemCount() > 0 ) {
				
				int numSizes = preferredSizes.length;
				boolean imageFound = false;
				for (int i = 0; i < numSizes; ++i) {
				
					if (imageFound) {
						break;
					}
				
					mImage = link.getImage(preferredSizes[i]);
					if (mImage != null) {
						imageFound = true;
					}
				}
			}
	}
	
	/**
	 * 
	 * @return If a valid image has been retrieved a byte array containing the image data is returned, otherwise null
	 */
	public byte[] getImageData() {
		
		return mImage;
	}
}
