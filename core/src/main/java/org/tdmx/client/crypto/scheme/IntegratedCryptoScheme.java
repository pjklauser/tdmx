/*
 * TDMX - Trusted Domain Messaging eXchange
 * 
 * Enterprise B2B messaging between separate corporations via interoperable cloud service providers.
 * 
 * Copyright (C) 2014 Peter Klauser (http://tdmx.org)
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/.
 */
package org.tdmx.client.crypto.scheme;

import org.tdmx.client.crypto.algorithm.DigestAlgorithm;
import org.tdmx.client.crypto.algorithm.KeyAgreementAlgorithm;
import org.tdmx.core.system.lang.EnumUtils;

public enum IntegratedCryptoScheme {

	ECDH384_RSA_SLASH_AES256__16MB_SHA1(
			"ecdh384:rsa/aes256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),
	ECDH384_RSA_SLASH_TWOFISH256__16MB_SHA1(
			"ecdh384:rsa/twofish256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),
	ECDH384_RSA_SLASH_SERPENT256__16MB_SHA1(
			"ecdh384:rsa/serpent256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),

	ECDH384_AES256plusRSA_SLASH_AES256__16MB_SHA1(
			"ecdh384:aes256+rsa/aes256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),
	ECDH384_AES256plusRSA_SLASH_TWOFISH256__16MB_SHA1(
			"ecdh384:aes256+rsa/twofish256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),
	ECDH384_AES256plusRSA_SLASH_SERPENT256__16MB_SHA1(
			"ecdh384:aes256+rsa/serpent256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),

	ECDH384_TWOFISH256plusRSA_SLASH_AES256__16MB_SHA1(
			"ecdh384:twofish256+rsa/aes256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),
	ECDH384_TWOFISH256plusRSA_SLASH_TWOFISH256__16MB_SHA1(
			"ecdh384:twofish256+rsa/twofish256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),
	ECDH384_TWOFISH256plusRSA_SLASH_SERPENT256__16MB_SHA1(
			"ecdh384:twofish256+rsa/serpent256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),

	ECDH384_SERPENT256plusRSA_SLASH_AES256__16MB_SHA1(
			"ecdh384:serpent256+rsa/aes256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),
	ECDH384_SERPENT256plusRSA_SLASH_TWOFISH256__16MB_SHA1(
			"ecdh384:serpent256+rsa/twofish256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1),
	ECDH384_SERPENT256plusRSA_SLASH_SERPENT256__16MB_SHA1(
			"ecdh384:serpent256+rsa/serpent256[16mbSHA1]",
			KeyAgreementAlgorithm.ECDH384,
			16 * EnumUtils.MB,
			DigestAlgorithm.SHA_1);

	private final String name;
	private final KeyAgreementAlgorithm sessionKeyAlgorithm;
	private final int chunkSize;
	private final DigestAlgorithm chunkMACAlgorithm;

	public KeyAgreementAlgorithm getSessionKeyAlgorithm() {
		return sessionKeyAlgorithm;
	}

	private IntegratedCryptoScheme(String name, KeyAgreementAlgorithm sessionKeyAlgorithm, int chunkSize,
			DigestAlgorithm chunkDigest) {
		this.name = name;
		this.sessionKeyAlgorithm = sessionKeyAlgorithm;
		this.chunkMACAlgorithm = chunkDigest;
		this.chunkSize = chunkSize;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public DigestAlgorithm getChunkMACAlgorithm() {
		return chunkMACAlgorithm;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	/**
	 * Returns a IntegratedCryptoScheme corresponding to the given name.
	 *
	 * @param name
	 *            The name of the IntegratedCryptoScheme
	 * @return IntegratedCryptoScheme representing the given name.
	 */
	public static IntegratedCryptoScheme fromName(String esName) {
		for (IntegratedCryptoScheme es : IntegratedCryptoScheme.values()) {
			if (es.getName().equals(esName)) {
				return es;
			}
		}
		return null;
	}

}
