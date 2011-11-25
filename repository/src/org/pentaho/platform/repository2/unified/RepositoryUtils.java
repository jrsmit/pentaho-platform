/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License, version 2 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * Copyright 2011 Pentaho Corporation. All rights reserved.
 */
package org.pentaho.platform.repository2.unified;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.pentaho.platform.api.repository2.unified.IRepositoryFileData;
import org.pentaho.platform.api.repository2.unified.IUnifiedRepository;
import org.pentaho.platform.api.repository2.unified.RepositoryFile;
import org.pentaho.platform.api.repository2.unified.RepositoryFileAcl;

/**
 * Utility methods that can be applied on any {@link }IUnifiedRepository}
 * User: dkincade
 */
public class RepositoryUtils {
  private IUnifiedRepository repository;

  /**
   * Creates a new instance of this utility class with access to the specified repository
   */
  public RepositoryUtils(final IUnifiedRepository repository) {
    if (null == repository) {
      throw new NullPointerException();
    }
    this.repository = repository;
  }

  /**
   * Returns the {@link IUnifiedRepository} for this instance
   */
  public IUnifiedRepository getRepository() {
    return repository;
  }

  /**
   * Returns the specified folder in the current repository. If it can not be found, it will optionally be
   * created. The missing parent folders can also be created if needed
   * @param path the full path to the requested folder in the repository
   * @param createIfNotExist {@code true} indicates the folder will be created if it doesn't exist
   * @param createParents {@code true} indicates that any missing parent folders will be created if they do not exist
   * @param versionMessage the message used if any folders are created
   * @return the {@link RepositoryFile} of the request folders, or {@code null} if it does not exist and the
   * parameters do not allow for it to be created
   */
  public RepositoryFile getFolder(final String path, final boolean createIfNotExist, final boolean createParents,
                                  final String versionMessage) {
    return getFolder(path, null, createIfNotExist, createParents, versionMessage);
  }

  public RepositoryFile getFolder(final String path, final RepositoryFileAcl acl, final boolean createIfNotExist,
                        final boolean createParents, final String versionMessage) {
    RepositoryFile folder = repository.getFile(path);
    if (null == folder && createIfNotExist) {
      final String parentPath = getParentPath(path);
      if (null != parentPath) {
        final RepositoryFile parentFolder = getFolder(parentPath, createParents, createParents, versionMessage);
        if (null != parentFolder) {
          final String folderName = FilenameUtils.getName(path);
          folder = new RepositoryFile.Builder(folderName).path(path).folder(true).build();
          if (null != acl) {
            folder = repository.createFolder(parentFolder.getId(), folder, acl, versionMessage);
          } else {
            folder = repository.createFolder(parentFolder.getId(), folder, versionMessage);
          }
        }
      }
    }
    return folder;
  }

  /**
   * Returns the specified file from the current repository. If it can not be found, it will optionally be
   * created. The missing parent folders can also be created if needed.
   * @param path the full path to the requested file in the repository
   * @param data the data to put in the file if it does not exist ... use {@code null} at your discretion
   * @param createIfNotExist {@code true} indicates the file will be created if it doesn't exist
   * @param createParents {@code true} indicates that any missing parent folders will be created if they do not exist
   * @param versionMessage the message used if the file or any folders are created
   * @return the {@link RepositoryFile} of the request file, or {@code null} if it does not exist and the
   * parameters do not allow for it to be created
   */
  public RepositoryFile getFile(final String path, final IRepositoryFileData data,
                                final boolean createIfNotExist, final boolean createParents,
                                final String versionMessage) {
    // Get the requested folder
    RepositoryFile file = repository.getFile(path);
    if (null == file && createIfNotExist) {
      final String parentFolderName = getParentPath(path);
      final RepositoryFile parentFolder = getFolder(parentFolderName, createParents, createParents, versionMessage);
      if (null != parentFolder) {
        final String fileName = FilenameUtils.getName(path);
        file = new RepositoryFile.Builder(fileName).folder(false).build();
        file = repository.createFile(parentFolder.getId(), file, data, versionMessage);
      }
    }
    return file;
  }

  /**
   * Due to a bug in {@code FilenameUtils.getPathNoEndSeparator()}, we will have to do this a little differently
   */
  protected static String getParentPath(String path) {
    final String prefix = FilenameUtils.getPrefix(path);
    if (StringUtils.equals(path, prefix)) {
      // We are at the root
      return null;
    }
    path = FilenameUtils.getPath(path);
    path = prefix + StringUtils.left(path, path.length() - 1);
    return path;
  }

}